package com.scalapenos.demo

import akka.actor.ActorSystem
import akka.io.IO

import spray.can.Http

import akka.actor.Props

object Main extends App
{
	implicit val system = ActorSystem("demo")

	//val api = system.actorOf(ApiActor.props, "api-actor")
	val sc = "Test global prop"
	val api = system.actorOf(Props(new ApiActor(sc)))
	IO(Http) ! Http.Bind(listener = api,
		interface = "0.0.0.0",
		port = 5000)
}
