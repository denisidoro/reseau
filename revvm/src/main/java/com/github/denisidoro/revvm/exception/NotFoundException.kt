package com.github.denisidoro.revvm.exception

open class NotFoundException: RevvmException()
class ControllerNotFoundException: NotFoundException()
class RootCastException: NotFoundException()
class ObservableCastException: NotFoundException()