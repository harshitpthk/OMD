from django.db import models

# Create your models here.
class Team(models.Model):
    teamName = models.CharField(max_length=80)
    def __str__(self):              # __unicode__ on Python 2
        return self.teamName

class Member(models.Model):
    memberName = models.CharField(max_length=80)
    memberEID = models.CharField(max_length=7)
    memberEmail = models.CharField(max_length=50)
    memberTeam = models.ForeignKey(Team,related_name='members')
    def __str__(self):              # __unicode__ on Python 2
        return self.memberName

class Device(models.Model):
    deviceName = models.CharField(max_length=50)
    deviceModel = models.CharField(max_length=40)
    deviceOS = models.CharField(max_length=40)
    deviceArch = models.CharField(max_length=40)
    deviceMem = models.CharField(max_length=20)
    deviceProc = models.CharField(max_length=20)
    deviceEquipID = models.CharField(max_length=20)
    deviceOwnerID = models.ForeignKey(Member,related_name='owner')
    deviceCurrOwnerID = models.ForeignKey(Member,related_name='current_owner')
    def __str__(self):              # __unicode__ on Python 2
        return self.deviceName
