from django.http import JsonResponse
from django.views.decorators.csrf import csrf_exempt
from .models import CustomUser, UserSession, Favorite
import bcrypt, json, secrets

SESSION_TOKEN_HEADER = 'Session-Token'


import json
import bcrypt
from django.http import JsonResponse
from django.views.decorators.csrf import csrf_exempt
from .models import CustomUser


@csrf_exempt
def register(request):
    if request.method != 'POST':
        return JsonResponse({'error': 'Unsupported HTTP method'}, status=405)
    body = json.loads(request.body)
    new_username = body.get('username', None)
    if new_username == None:
        return JsonResponse({'error': 'Missing username in request body'}, status=400)
    new_email = body.get('email', None)
    if new_email == None:
        return JsonResponse({'error': 'Missing email in request body'}, status=400)
    try:
        CustomUser.objects.get(username=new_username)
        CustomUser.objects.get(email=new_email)
    except CustomUser.DoesNotExist:
        # Proceed
        new_password = body.get('password', None)
        if new_password == None:
            return JsonResponse({'error': 'Missing password in request body'}, status=400)

        encrypted_pass = bcrypt.hashpw(new_password.encode('utf8'), bcrypt.gensalt()).decode('utf8')
        new_user = CustomUser()
        new_user.username = new_username
        new_user.email = new_email
        new_user.encrypted_password = encrypted_pass
        new_user.save()
        return JsonResponse({'created': 'True'}, status=201)

    # User DOES exist.
    return JsonResponse({'error': 'User with given username or email already exists'}, status=409)


@csrf_exempt
def login(request):
    if request.method != 'POST':
        return JsonResponse({'error': 'Unsupported HTTP method'}, status=405)
    body = json.loads(request.body)
    username = body.get('username', None)
    if username == None:
        return JsonResponse({'error': 'Missing username in request body'}, status=400)
    try:
        user = CustomUser.objects.get(username=username)
    except CustomUser.DoesNotExist:
        return JsonResponse({'error': 'Username does not exist'}, status=404)
    password = body.get('password', None)
    if password == None:
        return JsonResponse({'error': 'Missing password in request body'}, status=400)
    if bcrypt.checkpw(password.encode('utf8'), user.encrypted_password.encode('utf8')):
        new_session = UserSession()
        new_session.user = user
        new_session.token = secrets.token_hex(10)
        new_session.save()
        return JsonResponse({'created': 'True', 'sessionId': new_session.id, 'sessionToken': new_session.token},
                            status=201)
    else:
        return JsonResponse({'error': 'Password is invalid'}, status=401)

@csrf_exempt
def add_favorite(request):
    if request.method != 'POST':
        return JsonResponse({'error': 'Unsupported HTTP method'}, status=405)

    body = json.loads(request.body)
    session_token = request.headers.get(SESSION_TOKEN_HEADER)
    if not session_token:
        return JsonResponse({'error': 'Missing session token'}, status=401)

    try:
        session = UserSession.objects.get(token=session_token)
        user = session.user
    except UserSession.DoesNotExist:
        return JsonResponse({'error': 'Invalid session token'}, status=401)

    title = body.get('title', None)
    url = body.get('url', None)
    if not title or not url:
        return JsonResponse({'error': 'Missing title or URL in request body'}, status=400)

    description = body.get('description', '')
    image = body.get('image', '')

    favorite = Favorite(user=user, title=title, url=url, description=description, image=image)
    favorite.save()

    return JsonResponse({'message': 'Favorite added successfully'}, status=201)

@csrf_exempt
def get_favorites(request):
    if request.method != 'GET':
        return JsonResponse({'error': 'Unsupported HTTP method'}, status=405)

    session_token = request.headers.get(SESSION_TOKEN_HEADER)
    if not session_token:
        return JsonResponse({'error': 'Missing session token'}, status=401)

    try:
        session = UserSession.objects.get(token=session_token)
        user = session.user
    except UserSession.DoesNotExist:
        return JsonResponse({'error': 'Invalid session token'}, status=401)

    favorites = Favorite.objects.filter(user=user)
    favorites_list = [
        {
            'title': fav.title,
            'url': fav.url,
            'description': fav.description,
            'image': fav.image,
            'added_at': fav.added_at.isoformat()
        } for fav in favorites
    ]

    return JsonResponse({'favorites': favorites_list}, status=200)

