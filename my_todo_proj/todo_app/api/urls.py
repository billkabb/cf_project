from django.urls import path
from . views import *
from rest_framework.authtoken import views

urlpatterns = [
    # path('', home, name='home' ),
    # path('', get_todo, name='get_todo'),
    # path('post-todo/', post_todo, name='post_todo'),
    # path('patch-todo/', patch_todo, name='patch_todo'),

    path('', TodoListView.as_view()),
    path('<int:pk>/', TodoView.as_view()),
    path('register/', RegisterView.as_view()),
    path('login/', LoginView.as_view()),
    
]
urlpatterns += [
    path('gettoken/', views.obtain_auth_token)
]

