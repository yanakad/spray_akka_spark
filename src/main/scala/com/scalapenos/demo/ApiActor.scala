package com.scalapenos.demo

import akka.actor._

import spray.json._
import spray.json.DefaultJsonProtocol._
import spray.routing._

import util._
// import SprayJsonSupport._


import org.apache.spark._
import org.apache.spark.SparkContext._
import org.apache.spark.sql._

// 2 spaces indentation
case class Demo(greeting: String, name: String)

object Demo {
	implicit val jsonFormat = jsonFormat2(Demo.apply)
}

object ApiActor {
	def props = Props[ApiActor]
	// can pass function
}

//class ApiActor extends HttpServiceActor with ActorLogging
//class ApiActor(message:String) extends HttpServiceActor with ActorLogging

class ApiActor(sc:SparkContext) extends HttpServiceActor with ActorLogging
{
	def receive = runRoute(
		//pathPrefix("foo") {
		path("hello") {
			get {
//				complete(Demo("hello","world!"))
				//val message=sc.parallelize(1 to 500).sum
				val sqlContext = new SQLContext(sc)
        System.out.println(" ******Try parquet");
       sqlContext.parquetFile("/home/ykadiysk/Github/spark-1.2.1-bin-hadoop2.3/bin/large.parquet").registerTempTable("large");
    
        System.out.println(" ******Register done");
        val res = sqlContext.sql("select * from large LIMIT 1").collect
        println("HERE...")
        val message = res(0)
        complete(s"hello, world!... $message")
				// complete(404)
			} ~
			put {
			/*	entity(as[Demo]) { demo =>
					complete {
						Demo(demo.name, demo.greeting)
					}
				}*/
				complete("foo, bar!")
			}
		}
	)
}

