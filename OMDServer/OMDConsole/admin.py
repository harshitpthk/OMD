from django.contrib import admin

# Register your models here.
from .models import Device
from .models import Team
from .models import Member

admin.site.register(Device)
admin.site.register(Team)
admin.site.register(Member)
