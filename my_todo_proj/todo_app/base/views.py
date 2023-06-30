from typing import Any, Dict
from django.shortcuts import render, redirect
from django.views.generic.list import ListView
from django.views.generic.detail import DetailView
from django.views.generic.edit import CreateView, UpdateView, DeleteView, FormView
from django.urls import reverse_lazy # redirects
from .models import Todo
# authentication
from django.contrib.auth.views import LoginView
from django.contrib.auth.mixins import LoginRequiredMixin
from django.contrib.auth.forms import UserCreationForm
from django.contrib.auth import login

# Create your views here.

######### Authentication classes #########
# tha mporousa na ta exv se csexvristo registration app
class CustomLoginView(LoginView):
    template_name = "base/login.html"
    fields = '__all__'
    redirect_authenticated_user = True

    def get_success_url(self): # overide apo to RedirectUrlMixin
        return reverse_lazy('todos')
    
# https://www.pythontutorial.net/django-tutorial/django-formview/
class RegisterPage(FormView):
    template_name = 'base/register.html'
    form_class = UserCreationForm
    redirect_authenticated_user = True
    success_url = reverse_lazy('todos')

    def form_valid(self, form):
        user = form.save()
        if user is not None:
            login(self.request, user)
        return super(RegisterPage, self).form_valid(form)
    
    # redirect if the user already authenticated
    def get(self, *args, **kwargs):
        if self.request.user.is_authenticated:
            return redirect('tasks')
        return super(RegisterPage, self).get(*args, **kwargs)




############### crud classes ###############
class TodoList(LoginRequiredMixin, ListView):
    login_url = 'login' # redirects if the user is not logged in
    model = Todo
    context_object_name = 'todos' # default name is object_list , how i call the list from html

    def get_context_data(self, **kwargs): # filtering todos and pass extra var sto context (count)
        context =  super().get_context_data(**kwargs)
        context['todos'] = context['todos'].filter(user=self.request.user)
        context['count'] = context['todos'].filter(complete = False).count()

        # search_input = self.request.GET.get('search-area') or ''
        # if search_input:
        #     context['todos'] = context['todos'].filter(title__icontains=search_input) # case insensitive
        # context['search_input'] = search_input # make the search value persist
        return context

class TodoDetail(LoginRequiredMixin, DetailView):
    login_url = 'login'
    model = Todo
    context_object_name = 'todo'
    template_name = 'base/todo.html'

class TodoCreate(LoginRequiredMixin, CreateView):
    login_url = 'login'
    # todo_form.html
    model = Todo
    fields = ['title', 'description', 'complete']
    success_url = reverse_lazy('todos') # redirect after processing a valid form.

    def form_valid(self, form):
        form.instance.user = self.request.user # fernei mono ta todos to user
        return super().form_valid(form)

class TodoUpdate(LoginRequiredMixin, UpdateView):
    login_url = 'login'
    # uses also the todo_form.html by default
    model = Todo
    fields = ['title', 'description', 'complete']
    success_url = reverse_lazy('todos')

class DeleteView(LoginRequiredMixin, DeleteView):
    login_url = 'login'
    # todo_confirm_delete.html
    model = Todo
    context_object_name = 'todo' 
    success_url = reverse_lazy('todos')


    