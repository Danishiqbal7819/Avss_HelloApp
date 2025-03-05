package SqliteDatabase

//import android.graphics.BitmapRegionDecoder
//import kotlin.random.Random
//
//    enum class Cars(val model:Int,val company:String){
//    Baleno(2020,"Maruti Suzuki"),
//    Fronx(2021,"Maruti Suzuki"),
//    Santro(2023,"Hyundai"),
//    Eon(2024,"Hyundai")
//}
//
//fun car(car:Cars):String{
//    return car.toString()
//}

//fun main(){
//    Cars.values().forEach {
//        println(it.name)
//        println(it.model)
//        println(it.company)
//        println("-----------------------")
//    }
//    println(car(Cars.Baleno))
//    val random= Random(1).nextInt(10)+1
//    println(random)
//}

//sealed class School{
//    class TuitionFaculty(val id:String):School(){
//        enum class names(val id:String){
//            Rahul("233"),Radha("989"),Soniya("827"),Mohan("563")
//        }
//    }
//
//    class RegistrarFaculty(val id:String):School(){
//        enum class names(val id:String){
//            ghanshyam("112"),Rahat("323"),Loniya("232"),khan("434")
//        }
//    }
//
//    class TransportationFaculty(val id:String):School(){
//        enum class names(val id:String){
//            Pahul("156"),Sadhiya("982"),Roziya("673"),Sohan("496")
//        }
//    }}
//fun main(){
//    println("____________Tuition Faculty______________")
//    School.TuitionFaculty.names.values().forEach {
//        println("${it.ordinal+1}.name:${it}\n  id: ${it.id}")
//    }
//    println("____________ Registrar Faculty______________")
//
//    School.RegistrarFaculty.names.values().forEach {
//        println("${it.ordinal+1}.name:${it}\n  id: ${it.id}")
//    }
//
//    println("_____________Transportation Faculty______________")
//
//    School.TransportationFaculty.names.values().forEach {
//        println("${it.ordinal+1}.name:${it}\n  id: ${it.id}")
//    }
//}

// sealed class Shape{
//
//    abstract fun area():Double
//
//     class Circle(val radius:Double): Shape(){
//         override fun area():Double {
//             return Math.PI.times(radius).times(radius)
//
//         }
//     }
//
//     class Triangle(val base:Double,val Height:Double):Shape(){
//         override fun area(): Double {
//             return 0.5.times(base).times(Height)
//         }
//     }
//     class Rectangle(val length:Double,val Breadth:Double):Shape(){
//         override fun area(): Double {
//             return length.times(Breadth)
//         }
//     }
//}
//
//fun main(){
//    val circle= Shape.Circle(2.0)
//    val rectangle= Shape.Rectangle(2.0,4.0)
//    val triangle= Shape.Triangle(2.0,4.0)
//    println(circle.area())
//    println(rectangle.area())
//    println(triangle.area())
//}


//sealed class PaymentMethhod{
//    abstract fun describePaymentMethod (paymentMethhod: PaymentMethhod):String
//
//    class DebitCard(val cardNumber :String,val pin:String):PaymentMethhod(){
//        override fun describePaymentMethod (paymentMethhod: PaymentMethhod): String {
//          return "DebitCard with Number:$cardNumber and\n pin:$pin"
//        }
//
//    }
//
//    class CreditCard(val cardNumber:String, val expirationDate:String):PaymentMethhod(){
//        override fun describePaymentMethod (paymentMethhod: PaymentMethhod): String {
//            return "DebitCard with Number:$cardNumber and\n pin:$expirationDate"
//        }
//    }
//    class Paypal(val email1 :String,val  password:String): PaymentMethhod() {
//        override fun describePaymentMethod (paymentMethhod: PaymentMethhod): String {
//            return "Paypal Account With email:$email1 and\n password:$password"
//        }
//
//    }
//}
sealed class PaymentMethhod {
    class CreditCard(val cardNumber: String, val expirationDate: String) : PaymentMethhod()
    class DebitCard(val cardNumber: String, val pin: String) : PaymentMethhod()
    class Paypal(val email1: String, val password: String) : PaymentMethhod()
}


fun describePaymentMethod(paymentMethod: PaymentMethhod): String {
    return when (paymentMethod) {
        is PaymentMethhod.DebitCard -> "DebitCard with Number: ${paymentMethod.cardNumber} and pin: ${paymentMethod.pin}"
        is PaymentMethhod.CreditCard -> "CreditCard with Number: ${paymentMethod.cardNumber} and expirationDate: ${paymentMethod.expirationDate}"
        is PaymentMethhod.Paypal -> "Paypal Account With email: ${paymentMethod.email1} and password: ${paymentMethod.password}"
    }
}


fun main() {
    val debitcard = PaymentMethhod.DebitCard("3672672637", "1234")
    val creditcard = PaymentMethhod.CreditCard("5632GSJH0", "12/2034")
    val paypal = PaymentMethhod.Paypal("3672672637", "1234")

    println(describePaymentMethod(debitcard))
    println(describePaymentMethod(creditcard))
    println(describePaymentMethod(paypal))
}