from django.shortcuts import render
from rest_framework import status

from rest_framework.decorators import api_view, authentication_classes, permission_classes
from rest_framework.response import Response
from .serializer import TodoSerializer, UserSerializer
from base.models import Todo
from rest_framework.authentication import SessionAuthentication, BasicAuthentication, TokenAuthentication
from rest_framework.permissions import IsAuthenticated

from django.contrib.auth import get_user_model
from rest_framework.exceptions import AuthenticationFailed
from django.contrib.auth.models import User
import datetime
from rest_framework.authtoken.views import ObtainAuthToken
from rest_framework.authtoken.models import Token

from rest_framework.views import APIView


class RegisterView(APIView):
    def post(self, request):
        serializer = UserSerializer(data=request.data)
        serializer.is_valid(raise_exception=True)
        serializer.save()
        username = request.data['username']
        user = User.objects.filter(username=username).first()
        token = Token.objects.get(user=user)
        # return Response(serializer.data)
        return Response({
                # "message":"Success login",
                "token": token.key,
                # "user_id":user.pk,
                # "username":user.username
            })
    
class LoginView(APIView):
    def post(self, request):
        username = request.data['username']
        password = request.data['password']
        user = User.objects.filter(username=username).first()

        if user is None:
            raise AuthenticationFailed('User not found')
        if not user.check_password(password):
            raise AuthenticationFailed('Incorrect password')
        
        token = Token.objects.get(user=user)
        # token, created = Token.objects.get_or_create(user=user)
        return Response({
            # "message":"Success login",
            "token": token.key,
            # "user_id":user.pk,
            # "username":user.username
        })




# Create your views here.   
# @api_view(['GET', 'POST', 'PATCH'])
# def home(request):
#     if request.method == 'GET':
#         return Response({
#             'status':200,
#             'message': 'Yessss ',
#             'method_called': 'GET'
#         })
#     elif request.method == 'POST':
#         return Response({
#             'status':200,
#             'message': 'Yessss ',
#             'method_called': 'POST'
#         })
#     elif request.method == 'PATCH':
#         return Response({
#             'status':200,
#             'message': 'Yessss ',
#             'method_called': 'PATCH'
#         }) 
#     else:
#         return Response({
#             'status':400,
#             'message': 'Yessss ',
#             'method_called': 'invalid method'
#         }) 



# @api_view(['GET'])
# @authentication_classes([SessionAuthentication, BasicAuthentication])
# @permission_classes([IsAuthenticated])
# def get_todo(request):
#     todo_objs = Todo.objects.all().filter(user=request.user)
#     serializer = TodoSerializer(todo_objs, many=True)

#     return Response({
#         'status':True,
#         'message':'Todo fetched data',
#         'data':serializer.data
#     })

# @api_view(['POST'])
# @authentication_classes([SessionAuthentication, BasicAuthentication])
# @permission_classes([IsAuthenticated])
# def post_todo(request):
#     try:
#         data = request.data
#         # print(data)
#         data['user'] = request.user.id
#         serializer = TodoSerializer(data=data)
#         if serializer.is_valid():
#             # print(serializer.data)
            
#             serializer.save()
#             return Response({
#                 'status': True,
#                 'message': 'Success data',
#                 'data': serializer.data
#             })
#         return Response({
#             'status':False,
#             'message':'invalid data',
#             'data': serializer.errors
#         })
#     except Exception as e:
#         print(e)

#     return Response({
#         'status':400,
#         'message': 'something went wrong'
#     })

# @api_view(['PATCH'])
# @authentication_classes([SessionAuthentication, BasicAuthentication])
# @permission_classes([IsAuthenticated])
# def patch_todo(request):
#     try:
#         data = request.data
#         data['user'] = request.user.id
#         if not data.get('id'):
#             return Response({
#                 'status':False,
#                 'message': 'id is required',
#                 'data':{}
#             })
#         obj = Todo.objects.get(id=data.get('id'))
#         serializer = TodoSerializer(obj, data=data, partial=True)
#         if serializer.is_valid():
#             serializer.save()
#             return Response({
#                 'status': True,
#                 'message': 'Patch succesfull',
#                 'data': serializer.data
#             })
        
#         return Response({
#             'status':False,
#             'message':'invalid data',
#             'data': serializer.errors
#         })
#     except Exception as e:
#         print(e)

#     return Response({
#             'status':False,
#             'message':'invalid id',
#             'data': {}
#         })

##############APIView#############
class TodoListView(APIView):
    authentication_classes = [TokenAuthentication]
    permission_classes = [IsAuthenticated]

    

    def get(self, request):
        # print(request.user)
        # User = get_user_model()
        # users = User.objects.all()

        todo_objs = Todo.objects.filter(user= request.user)

        serializer = TodoSerializer(todo_objs, many=True)

        return Response({
            # 'status':True,
            # 'message':'Todo fetched data',
            'username':str(request.user),
            # 'users': str(users),
            'data':serializer.data
        })
        # return Response(data=serializer.data)
    def post(self, request):
        try:
            data = request.data
            # print(data)
            data['user'] = request.user.id
            serializer = TodoSerializer(data=data)
            if serializer.is_valid():
                # print(serializer.data)
                serializer.save()
                return Response({
                    'status': True,
                    'message': 'Success data',
                    'data': serializer.data
                })
            return Response({
                'status':False,
                'message':'invalid data',
                'data': serializer.errors
            })
        except Exception as e:
            print(e)

        return Response({
            'status':400,
            'message': 'something went wrong'
        })
    

class TodoView(APIView):
    authentication_classes = [TokenAuthentication]
    permission_classes = [IsAuthenticated]

    

    def get(self, request, pk):
        # print(request.user)
        # User = get_user_model()
        # users = User.objects.all()

        todo_objs = Todo.objects.filter(id=pk)

        serializer = TodoSerializer(todo_objs, many=True)

        return Response({
            'status':True,
            'message':'Todo fetched data',
            'user':str(request.user),
            # 'users': str(users),
            'data':serializer.data
        })

    
    # def post(self, request):
    #     try:
    #         data = request.data
    #         # print(data)
    #         data['user'] = request.user.id
    #         serializer = TodoSerializer(data=data)
    #         if serializer.is_valid():
    #             # print(serializer.data)
    #             serializer.save()
    #             return Response({
    #                 'status': True,
    #                 'message': 'Success data',
    #                 'data': serializer.data
    #             })
    #         return Response({
    #             'status':False,
    #             'message':'invalid data',
    #             'data': serializer.errors
    #         })
    #     except Exception as e:
    #         print(e)

    #     return Response({
    #         'status':400,
    #         'message': 'something went wrong'
    #     })
    
    def patch(self, request, pk):
        try:
            
            data = request.data
            # data._mutable=True # accept the request in form data
            data['user'] = request.user.id
            # if not data.get('id'):
            #     return Response({
            #         'status':False,
            #         'message': 'id is required',
            #         'data':{}
            #     })
            # obj = Todo.objects.get(id=data.get('id'))
            obj = Todo.objects.get(id=pk)
            serializer = TodoSerializer(obj, data=data, partial=True)
            if serializer.is_valid():
                serializer.save()
                # return Response({
                #     'status': True,
                #     'message': 'Patch succesfull',
                #     'data': serializer.data
                # })
                return Response(data=serializer.data)
        
            return Response({
                'status':False,
                'message':'invalid data',
                'data': serializer.errors
            })
        except Exception as e:
            print(e)

        return Response({
                'status':False,
                'message':'invalid id',
                'data': {}
            })
    
        
    
    def delete(self, request, pk):
        try:
            data = request.data
            # obj = Todo.objects.get(id=data.get('id'))
            obj = Todo.objects.get(id=pk)
            if obj.user.id == request.user.id:
                obj.delete()
                return Response({
                'status':200,
                'message': 'object deleted ',
                'method_called': 'delete',
                'todo deleted': obj.title
                })
            # return Response(status=status.HTTP_204_NO_CONTENT)
            else:
                return Response({
                    'status':400,
                    'message': 'not authorised to delete this todo ',
                    'method_called': 'delete'
                    # 'Data': obj.get('id')
                })
        except Exception as e:
            print(e)
        return Response({
                'status':False,
                'message':'Todo not exist',
                'data': {}
            })
