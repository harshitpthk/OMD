# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='Device',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('deviceName', models.CharField(max_length=50)),
                ('deviceModel', models.CharField(max_length=40)),
                ('deviceOS', models.CharField(max_length=40)),
                ('deviceArch', models.CharField(max_length=40)),
                ('deviceMem', models.CharField(max_length=20)),
                ('deviceProc', models.CharField(max_length=20)),
                ('deviceEquipID', models.CharField(max_length=20)),
            ],
        ),
        migrations.CreateModel(
            name='Member',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('memberName', models.CharField(max_length=80)),
                ('memberEID', models.CharField(max_length=7)),
                ('memberEmail', models.CharField(max_length=50)),
            ],
        ),
        migrations.CreateModel(
            name='Team',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('teamName', models.CharField(max_length=80)),
            ],
        ),
        migrations.AddField(
            model_name='member',
            name='memberTeam',
            field=models.ForeignKey(related_name='members', to='OMDConsole.Team'),
        ),
        migrations.AddField(
            model_name='device',
            name='deviceCurrOwnerID',
            field=models.ForeignKey(related_name='current_owner', to='OMDConsole.Member'),
        ),
        migrations.AddField(
            model_name='device',
            name='deviceOwnerID',
            field=models.ForeignKey(related_name='owner', to='OMDConsole.Member'),
        ),
    ]
