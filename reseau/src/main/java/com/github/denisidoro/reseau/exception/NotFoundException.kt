package com.github.denisidoro.reseau.exception

open class NotFoundException: ReseauException()
class ControllerNotFoundException: NotFoundException()
class RootCastException: NotFoundException()
class ObservableCastException: NotFoundException()