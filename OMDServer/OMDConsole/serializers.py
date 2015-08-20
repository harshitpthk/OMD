from OMDConsole.models import Team,Device,Member
from rest_framework import routers, serializers, viewsets
class DeviceSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = Device
        fields = ('url','id','deviceName','deviceModel','deviceOS','deviceArch','deviceMem','deviceProc','deviceEquipID','deviceOwnerID','deviceCurrOwnerID')

class DeviceViewSet(viewsets.ModelViewSet):
    queryset =Device.objects.all()
    serializer_class = DeviceSerializer

#serializer class for serializing member objects without owned devices included
class MemberSerializer(serializers.HyperlinkedModelSerializer):
    # owner = DeviceSerializer(many=True)
    class Meta:
        model = Member
        fields = ('url','memberName','memberEID','memberEmail','memberTeam')

#viewset class for Members without owned devices
class MemberViewSet(viewsets.ModelViewSet):
    queryset = Member.objects.all()
    serializer_class = MemberSerializer


#serializer class for serializing member objects with owned devices included
class MemberDeviceSerializer(serializers.HyperlinkedModelSerializer):
    owner = DeviceSerializer(many=True)
    class Meta:
        model = Member
        fields = ('url','id','memberName','memberEID','memberEmail','memberTeam','owner')

#viewset class for Members with owned devices
class MemberDeviceViewSet(viewsets.ModelViewSet):
    queryset = Member.objects.all()
    serializer_class = MemberDeviceSerializer

#serializer class for serializing team objects without members included
class TeamSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = Team
        fields = ('url','id','teamName')


#viewset class for Teams without Members
class TeamViewSet(viewsets.ModelViewSet):
    queryset = Team.objects.all()
    serializer_class = TeamSerializer


#serializer class for serializing team objects with members included
class TeamMemberSerializer(serializers.HyperlinkedModelSerializer):
    members = MemberSerializer(many=True)
    class Meta:
        model = Team
        fields = ('url','id','teamName','members')


#viewset class for Teams with Members
class TeamMemberViewSet(viewsets.ModelViewSet):
    queryset = Team.objects.all()
    serializer_class = TeamMemberSerializer
