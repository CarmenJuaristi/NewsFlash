from django.db import models


class CustomUser(models.Model):
    username = models.CharField(unique=True, max_length=50)
    encrypted_password = models.CharField(max_length=100)
    email = models.CharField(unique=True, max_length=200)

    def __str__(self):
        return self.username


class UserSession(models.Model):
    user = models.ForeignKey(CustomUser, on_delete=models.CASCADE)
    token = models.CharField(unique=True, max_length=20)

    def __str__(self):
        return str(self.user) + ' - ' + self.token

class Favorite(models.Model):
    user = models.ForeignKey(CustomUser, on_delete=models.CASCADE)
    news_id = models.CharField(max_length=255)  # ID único o URL de la noticia
    added_at = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return f"{self.user.username} - {self.news_id}"



