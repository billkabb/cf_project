from django.shortcuts import render
from rest_framework import status

from rest_framework.decorators import api_view, authentication_classes, permission_classes
from rest_framework.response import Response
from .serializer import TodoSerializer
from base.models import Todo
from rest_framework.authentication import SessionAuthentication, BasicAuthentication, TokenAuthentication
from rest_framework.permissions import IsAuthenticated

from rest_framework.views import APIView


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

class TodoView(APIView):
    authentication_classes = [TokenAuthentication]
    permission_classes = [IsAuthenticated]

    def get(self, request):
        # print(request.user)
        todo_objs = Todo.objects.filter(user= request.user)

        serializer = TodoSerializer(todo_objs, many=True)

        return Response({
            'status':True,
            'message':'Todo fetched data',
            'user':str(request.user),
            'data':serializer.data
        })
    
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
    
    def patch(self, request):
        try:
            data = request.data
            data['user'] = request.user.id
            if not data.get('id'):
                return Response({
                    'status':False,
                    'message': 'id is required',
                    'data':{}
                })
            obj = Todo.objects.get(id=data.get('id'))
            serializer = TodoSerializer(obj, data=data, partial=True)
            if serializer.is_valid():
                serializer.save()
                return Response({
                    'status': True,
                    'message': 'Patch succesfull',
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
                'status':False,
                'message':'invalid id',
                'data': {}
            })
    
    def delete(self, request):
        try:
            data = request.data
            obj = Todo.objects.get(id=data.get('id'))
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
