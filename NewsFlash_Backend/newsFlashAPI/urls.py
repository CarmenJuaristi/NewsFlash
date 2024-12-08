
from django.contrib import admin
from django.urls import path
from newFlashApp import endpoints
urlpatterns = [
    path('api/admin/', admin.site.urls),
    path('api/register/', endpoints.register, name='register'), # Registro de usuarios
    path('api/login/', endpoints.login, name='login'),  # Inicio de sesión
    path('api/favorites/add/', endpoints.add_favorite, name='add_favorite'),  # Agregar a favoritos
    path('api/favorites/', endpoints.get_favorites, name='get_favorites'),  # Obtener lista de favoritos
]
