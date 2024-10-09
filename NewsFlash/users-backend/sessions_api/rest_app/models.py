from django.db import models
class CustomUser(models.Model):
    name = models.CharField(unique=True,max_length=50)
    apellidos = models.CharField(unique=True,max_length=100)
    encrypted_password = models.CharField(max_length=100)
    email = models.CharField(unique=True, max_length=100)

    def __str__(self):
        return self.name

class UserSession(models.Model):
    user = models.ForeignKey(CustomUser,on_delete=models.CASCADE)
    token = models.CharField(unique=True, max_length=20)

    def __str__(self):
        return str(self.user) + ' - '+ self.token

