from django.urls import path
from .views import *
from django.contrib.auth.views import LogoutView # import directly
from rest_framework.authtoken import views

urlpatterns = [
   
    path('login/', CustomLoginView.as_view(), name='login'),
    path('logout/', LogoutView.as_view(next_page='login'), name='logout'),
    path('register/', RegisterPage.as_view(), name='register'),
    
    path('', TodoList.as_view(), name='todos'),
    path('todo/<int:pk>/',TodoDetail.as_view(), name='todo'),
    path('todo-update/<int:pk>/',TodoUpdate.as_view(), name='todo-update'),
    path('todo-delete/<int:pk>/',DeleteView.as_view(), name='todo-delete'),
    path('todo-create/',TodoCreate.as_view(), name='todo-create'),
]
# urlpatterns += [
#     path('token/', views.obtain_auth_token)
# ]