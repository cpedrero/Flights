package com.stratio.model.spark

import com.stratio.model.{Delays, Flight, FuelPrice}
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.joda.time.LocalDate

import scala.language.implicitConversions

class FlightCsvReader(self: RDD[String]) {

  /**
    *
    * Parsea el RDD[String] de CSV's a un RDD[Flight]
    *
    * Tip: Usar método flatmap.
    *
    */

  /*def toFlight: RDD[Flight] = {
    self.map(x => {
      val a = x.split(",")
    /*  //a.foreach { letra:String => println(letra) }*/
      println(x)
      val b = Flight.apply(a)
        b
    })
  }*/

  def toFlight: RDD[Flight] = {
    self.flatMap(line => {
      Array(Flight.apply(line.split(",")))
    })
  }

  /**
    *
    * Obtener todos los errores del CSV asociados a cada línea. OJO puede haber más de un error por línea
    *
    * Tip: Usar método flatmap.
    * Tip: Usar el método extractErrors de Flight
    *
    */
  def toErrors: RDD[(String, String)] = {
    val lineWithIndex = self.zipWithIndex()
    lineWithIndex.flatMap(
      lineTuple => {
        val seqErrors = Flight.extractErrors(lineTuple._1.split(","))
        val lineNumber = lineTuple._2
        seqErrors.map(line=>(line,lineNumber.toString))
        //seqErrors.flatMap(line=>Array((line,lineNumber.toString)))
      }).filter(tuple=>tuple._1.nonEmpty)
  }
}
class FlightFunctions(self: RDD[Flight]) {

  /**
   *
   * Obtener la distancia media recorrida por cada aeropuerto.
   *
   *
   */
  def averageDistanceByAirport: RDD[(String, Float)] = {
    ???
  }

  /**
   *
   * Obtener el consumo mínimo por aeropuerto, mes y año.
   * @param fuelPrice RDD que contiene el precio del Fuel en un año
   *                  y mes determinado. Ver case class {@see com.stratio.model.FuelPrice}
   *
   *  Tip: Primero agrupar para cada aeropuerto, mes y año y sumar las distancias de los vuelos por el precio de
   *  combustible para ese mes y año y luego ver, para cada aeropuerto cual es el menor de los totales de los meses, año
   *
   *  Tip: Si el RDD es muy pequeño, podeis usar variables compartidas para evitar Joins
   *
   */
  def minFuelConsumptionByMonthAndAirport(fuelPrice: RDD[FuelPrice]): RDD[(String, (Int, Int))] = {
   ???
  }
}


trait FlightDsl {

  implicit def flightParser(lines: RDD[String]): FlightCsvReader = new FlightCsvReader(lines)

  implicit def flightFunctions(flights: RDD[Flight]): FlightFunctions = new FlightFunctions(flights)
}

object FlightDsl extends FlightDsl


