package etl.util


    private fun generateFlightData(count: Int) {
        val airlineCode = "PD"
        val headers = listOf(
            "flight_id","origin","destination","departure_hour","departure_minute",
            "arrival_hour","arrival_minute","delay_minutes","status","passenger_count","load_factor"
        )

        val origins = listOf("YYZ","YTZ","YUL","YOW","YVR")
        val destinations = listOf("YOW","YYZ","YUL","YVR","LAX")
        val statuses = listOf("ON_TIME","DELAYED","CANCELLED")

        val lines = mutableListOf<String>()
        lines.add(headers.joinToString(","))

        for (i in 1..count) {

            val isBad = i % 10 == 0 || i % 13 == 0  // ~10-15% hatalı

            val origin = if (isBad && i % 2 == 0) "" else origins.random()
            val departureMinute = if (isBad && i % 3 == 0) 75 else (0..59).random()
            val delay = if (isBad && i % 5 == 0) -10 else (0..20).random()
            val load = if (isBad && i % 7 == 0) 120 else (60..100).random()

            val row = listOf(
                "$airlineCode${100 + i}",
                origin,
                destinations.random(),
                (0..23).random(),
                departureMinute,
                (0..23).random(),
                (0..59).random(),
                delay,
                statuses.random(),
                (50..200).random(),
                load
            )

            lines.add(row.joinToString(","))
        }

        println(lines.joinToString("\n"))
    }

    fun main(){
        generateFlightData(85)
    }
