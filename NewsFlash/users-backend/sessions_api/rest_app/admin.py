from django.contrib import admin
from .models import UserSession
from .models import UserVideo
from .models import CustomUser
admin.site.register(UserSession)
admin.site.register(UserVideo)
admin.site.register(CustomUser)

# Register your models here.