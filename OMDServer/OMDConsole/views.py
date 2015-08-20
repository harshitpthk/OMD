from django.shortcuts import render
from django.http import HttpResponse
from django.views.decorators.csrf import csrf_exempt
from rest_framework.renderers import JSONRenderer
from rest_framework.parsers import JSONParser
from OMDConsole.models import Team,Member,Device
from OMDConsole.serializers import DeviceSerializer,MemberSerializer,TeamSerializer,TeamMemberSerializer,MemberDeviceSerializer
from django.core.mail import send_mail

# Create your views here.

class JSONResponse(HttpResponse):
    def __init__(self,data,**kwargs):
        content = JSONRenderer().render(data)
        kwargs['content_type'] = 'application/json'
        super(JSONResponse, self).__init__(content,**kwargs)

@csrf_exempt
def teamList(request,includeMembers):
    if request.method=='GET':
        teams = Team.objects.all()
        if includeMembers == 'yes':
            serializer  = TeamMemberSerializer(teams,many=True,context={'request': request})

        else:
            serializer = TeamSerializer(teams,many=True,context={'request':request})
        return JSONResponse(serializer.data)

@csrf_exempt
def addTeam(request):
    if request.method =='POST':
        data = JSONParser().parse(request)
        serializer = TeamSerializer(data=data,context={'request': request})
        if serializer.is_valid():
            serializer.save()
            return JSONResponse(serializer.data,status = 201)
        return JSONResponse("invalid_data",status = 400)

@csrf_exempt
def getTeam(request, pk):
    try:
        team = Team.objects.get(pk=pk)
    except Team.DoesNotExist:
        return HttpResponse(status=404)
    if request.method == 'GET':
        serializer = TeamMemberSerializer(team,context={'request': request})
        return JSONResponse(serializer.data)
    return JSONResponse("invalid_data",status = 400)


@csrf_exempt
def updateTeam(request, pk):
    try:
        team = Team.objects.get(pk=pk)
    except Team.DoesNotExist:
        return HttpResponse(status=404)

    if request.method == 'PUT':
        data = JSONParser().parse(request)
        serializer = TeamSerializer(team,data=data,context={'request': request})
        if serializer.is_valid():
            serializer.save()
            return JSONResponse(serializer.data,status = 201)
        return JSONResponse("invalid_data",status = 400)
    return JSONResponse("invalid_data",status = 400)


@csrf_exempt
def deleteTeam(request,pk):
    try:
        team = Team.objects.get(pk=pk)
    except Team.DoesNotExist:
        return HttpResponse(status=404)

    if request.method == 'DELETE':
        team.delete()
        result = {}
        result['result'] ='success'
        return JSONResponse(result,status=204)
    return JSONResponse("invalid_data",status = 400)

#
#   member views
#
@csrf_exempt
def memberList(request,includeDevices):
    if request.method=='GET':
        members = Member.objects.all()
        if includeDevices == 'yes':
            serializer = MemberDeviceSerializer(members,many=True,context={'request': request})
        else:
            serializer  = MemberSerializer(members,many=True,context={'request': request})
        return JSONResponse(serializer.data)
    return JSONResponse("invalid_data",status = 400)

@csrf_exempt
def getMember(request,pk):
    try:
        member = Member.objects.get(pk=pk)
    except Member.DoesNotExist:
        return HttpResponse(status=404)

    if request.method == 'GET':
        serializer = MemberDeviceSerializer(member,context={'request': request})
        return JSONResponse(serializer.data)
    return JSONResponse("invalid_data",status = 400)

@csrf_exempt
def addMember(request):
    if request.method =='POST':
        data = JSONParser().parse(request)
        serializer = MemberSerializer(data=data,context={'request': request})
        if serializer.is_valid():
            serializer.save()
            return JSONResponse(serializer.data,status = 201)
        return JSONResponse("invalid_data",status = 400)

@csrf_exempt
def deleteMember(request,pk):
    try:
        member = Member.objects.get(pk=pk)
    except Member.DoesNotExist:
        return HttpResponse(status=404)
    if request.method =='DELETE':
        member.delete()
        result = {}
        result['result'] ='success'
        return JSONResponse(result,status=204)
    return JSONResponse("invalid_data",status = 400)


@csrf_exempt
def updateMember(request,pk):
    try:
        member = Member.objects.get(pk=pk)
    except Member.DoesNotExist:
        return HttpResponse(status=404)
    if request.method =='PUT':
        data = JSONParser().parse(request)
        serializer = MemberSerializer(member,data=data,context={'request': request})
        if serializer.is_valid():
            serializer.save()
            return JSONResponse(serializer.data,status = 201)
        return JSONResponse("invalid_data",status = 400)
    return JSONResponse("invalid_data",status = 400)

@csrf_exempt
def deviceList(request):
    if request.method=='GET':
        devices = Device.objects.all()
        serializer  = DeviceSerializer(devices,many=True,context={'request': request})
        return JSONResponse(serializer.data)

@csrf_exempt
def getDevice(request,pk):
    if request.method=='GET':
        try:
            device = Device.objects.get(pk=pk)
        except Device.DoesNotExist:
            return HttpResponse(status=404)
        serializer = MemberDeviceSerializer(member,context={'request': request})
        return JSONResponse(serializer.data)

    return JSONResponse("invalid_data",status = 400)

@csrf_exempt
def addDevice(request):
    if request.method=='POST':
        data = JSONParser().parse(request)
        serializer = DeviceSerializer(data=data,context={'request': request})
        if serializer.is_valid():
            serializer.save()
            return JSONResponse(serializer.data,status = 201)
        return JSONResponse("invalid_data",status = 400)

@csrf_exempt
def deleteDevice(request,pk):
    if request.method =='DELETE':
        try:
            device = Device.objects.get(pk=pk)
        except Device.DoesNotExist:
            return HttpResponse(status=404)
        device.delete()
        result = {}
        result['result'] ='success'
        return JSONResponse(result,status=204)
    return JSONResponse("invalid_data",status = 400)

@csrf_exempt
def updateDevice(request,pk):
    if request.method == 'PUT':
        try:
            device = Device.objects.get(pk=pk)
        except Device.DoesNotExist:
            return HttpResponse(status=404)

        currentOwner = device.deviceCurrOwnerID
        owner = device.deviceOwnerID
        data = JSONParser().parse(request)
        serializer = DeviceSerializer(device,data=data,context={'request': request})


        if serializer.is_valid():
            updatedDevice = serializer.save()
            newCurrentOwner = updatedDevice.deviceCurrOwnerID
            send_mail(device.deviceName + ' Ownership has been changed', 'Device Ownership for device '+ device.deviceName +' has been modified , new current owner is '+ newCurrentOwner.memberName ,'omdsap@gmail.com' ,
            [currentOwner.memberEmail], fail_silently=False)

            send_mail(device.deviceName + ' Ownership has been changed', 'Device Ownership for device '+ device.deviceName +' has been modified, new current owner is '+ newCurrentOwner.memberName ,'omdsap@gmail.com' ,
            [newCurrentOwner.memberEmail], fail_silently=False)

            send_mail(device.deviceName + ' Ownership has been changed', 'Device Ownership for device '+ device.deviceName +' has been modified , new current owner is '+ newCurrentOwner.memberName ,'omdsap@gmail.com' ,
            [owner.memberEmail], fail_silently=False)

            return JSONResponse(serializer.data,status = 201)
        return JSONResponse("invalid_data",status = 400)
    return JSONResponse("invalid request",status = 400)
