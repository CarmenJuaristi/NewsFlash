from django.contrib import admin
from .models import UserSession
from .models import CustomUser

admin.site.register(UserSession)
admin.site.register(CustomUser)

