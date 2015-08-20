from django.conf.urls import url
from OMDConsole import views

urlpatterns = [
    url(r'^teams/members/(?P<includeMembers>(yes|no))$', views.teamList),
    url(r'^team/add$',views.addTeam),
    url(r'^team/update/(?P<pk>[0-9]+)$',views.updateTeam),
    url(r'^team/delete/(?P<pk>[0-9]+)$',views.deleteTeam),
    url(r'^getteam/(?P<pk>[0-9]+)$',views.getTeam),
    url(r'^members/devices/(?P<includeDevices>(yes|no))$',views.memberList),
    url(r'^getmember/(?P<pk>[0-9]+)$',views.getMember),
    url(r'^member/add$',views.addMember),
    url(r'^member/update/(?P<pk>[0-9]+)$',views.updateMember),
    url(r'^member/delete/(?P<pk>[0-9]+)$',views.deleteMember),
    url(r'^devices$',views.deviceList),
    url(r'^device/add$',views.addDevice),
    url(r'^device/update/(?P<pk>[0-9]+)$',views.updateDevice),
    url(r'^device/delete/(?P<pk>[0-9]+)$',views.deleteDevice),
    url(r'^getDevice/(?P<pk>[0-9]+)$',views.getDevice),

]
