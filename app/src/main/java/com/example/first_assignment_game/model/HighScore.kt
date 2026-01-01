package com.example.first_assignment_game.model


data class HighScore (
    val name: String,
    val score: Int,
    val lat: Double,
    val lon: Double
) {
     class Builder(
         var name: String = "",
         var score: Int = 0,
         var lat: Double = 0.0,
         var lon: Double = 0.0
     )
     {
         fun name(name:String) = apply { this.name = name  }
         fun score(score:Int) = apply { this.score =  score}
         fun lat(lat:Double) = apply { this.lat = lat }
         fun lon(lon:Double) = apply { this.lon =  lon}
         
         fun build() = HighScore(
             name,
             score,
             lat,
             lon
         )
     }
}