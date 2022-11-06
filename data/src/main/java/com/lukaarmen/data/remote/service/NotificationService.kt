package com.lukaarmen.data.remote.service

import com.lukaarmen.data.remote.dto.NotificationBodyDto
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationService {

    @Headers("Authorization: key=AAAAo9FQ9oA:APA91bHq8NpmmdZSUPSJ3O2e6c0HCjNn_KwYTaP1jg9VPyoVat60HHahNtuz2mv_k_wCR6bZAe156y15Lj5mNgg6caOjC-_a8qxpVaIKKTHnRRZ50CR2VrB0XLxfVZzMjNfKqc0HcI6f")
    @POST("fcm/send")
    suspend fun sendNotification(@Body body: NotificationBodyDto)
}