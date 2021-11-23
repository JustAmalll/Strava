package com.skillbox.core_network.utils

sealed class Failure {
    //Просим пользователя повторно выполнить запрос
    object ServerError : Failure()

    //Переход на экран авторизации, кэш сервера протух
    object CacheError : Failure()

    //Подгружаем локальные данные
    object LocalSuccess : Failure()
}
