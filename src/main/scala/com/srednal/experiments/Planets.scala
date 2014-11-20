package com.srednal.experiments

case class Planet(name: String,
                  number: Int,  // Mercury=0...
                  mass: Double, // 10^24 kg
                  diameter: Long, // km
                  gravity: Double, // m/s^2
                  dayLength: Double, //hours
                  distanceFromSun: Double, // 10^6km
                  orbitPeriod: Double, //days
                  tilt: Double, // deg
                  meanTemp: Int, // C
                  rings: Boolean) {
  def moons = Moons(name)
}

object Planets {
  def apply() = planets
  def apply(name: String) =planets find (_.name == name)
  def apply(n: Int) = planets(n)

 val planets =  List(
   Planet("Mercury",0,0.33,4879,3.7,4222.6,57.9,88.0,0.01,167,false),
   Planet("Venus",1, 4.87,12104,8.9,2802.0,108.2,224.7,177.4,464,false),
   Planet("Earth",2, 5.97,12756,9.8,24.0,149.6,365.2,23.4,15,false),
   Planet("Mars",3, 0.642,6792,3.7,24.7,227.9,687.0,25.2,-65,false),
   Planet("Jupiter",4, 1898.0,142984,23.1,9.9,778.6,4331.0,3.1,-110,true),
   Planet("Saturn",5, 568.0,120536,9.0,10.7,1433.5,10747.0,26.7,-140,true),
   Planet("Uranus",6, 86.8,51118,8.7,17.2,2872.5,30589.0,97.8,-195,true),
   Planet("Neptune",7,102.0,49528,11.0,16.1,4495.1,59800.0,28.3,-200,true))
}

