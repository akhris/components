package test

import domain.entities.Container

object Containers {
    val room407 = Container(name = "room 407", description = "4th floor")
    val shelving1 =
        Container(name = "shelving 1", description = "first metal shelving on the left", parentContainer = room407)
    val box1 =
        Container(name = "box 1", description = "small plastic box with white cover", parentContainer = shelving1)
    val box2 =
        Container(name = "box 2", description = "medium plastic box with red cover", parentContainer = shelving1)
    val box3 =
        Container(name = "box 3", description = "bix plastic box with blue cover", parentContainer = shelving1)

}