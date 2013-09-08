package com.dennishorte

import org.scalatest.FreeSpec
import org.scalatest.matchers.MustMatchers

class HashTableSpec extends FreeSpec with MustMatchers {

  "HashTable" - {

    "when empty" - {

      trait Fixture {
        val h = new HashTable[String, String]()
      }

      "has size 0" in new Fixture {
        assert(h.size === 0)
      }

      "can insert elements using update method" in new Fixture {
        h("hello") = "world"
        assert(h contains "hello")
        assert(h.size === 1)
      }

      "can insert elements using += method" in new Fixture {
        h += "hello" -> "world"
        assert(h contains "hello")
        assert(h.size === 1)
      }
    }

    "with one element" - {

      trait Fixture {
        val h = new HashTable[String, String]()
        h("foo") = "bar"
      }

      "can insert elements using update method" in new Fixture {
        h("hello") = "world"
        assert(h contains "hello")
        assert(h.size === 2)
      }

      "can insert elements using += method" in new Fixture {
        h += "hello" -> "world"
        assert(h contains "hello")
        assert(h.size === 2)
      }

      "can update elements using update method" in new Fixture {
        h("foo") = "world"
        assert(h("foo") == "world")
        assert(h.size === 1)
      }

      "can update elements using += method" in new Fixture {
        h += "foo" -> "world"
        assert(h("foo") == "world")
        assert(h.size === 1)
      }

      "can remove an element" in new Fixture {
        h -= "foo"
        assert(h.size === 0)
        assert(h.get("foo").isEmpty)
      }
    }

    "after resizing" - {

      trait Fixture {
        val elements = List(
          "foo" -> "bar",
          "hello" -> "world",
          "1" -> "one",
          "2" -> "two",
          "elephant" -> "monkey",
          "potato" -> "tomato",
          "tomato" -> "potato",
          "up" -> "down",
          "yesterday" -> "tomorrow"
        )

        val h = new HashTable[String, String](11)
        elements.foreach(h.+=)
      }

      "still has the correct number of elements" in new Fixture {
        assert(h.size === elements.size)
      }

      "has all elements mapped to correct value" in new Fixture {
        assert(
          elements.forall{ case (key, value) => h(key) == value }
        )
      }
    }

    "with keys that have negative hash values" - {
      "assigns a valid index" in {
        val h = new HashTable[AnyRef, String]()
        case object Obj1 {
          override val hashCode = -2
        }

        h(Obj1) = "negative"  // should not throw exception
      }
    }

    "with colliding keys" - {

      trait Fixture {
        case object Obj1 {
          override val hashCode = 1
        }
        case object Obj2 {
          override val hashCode = 1
        }

        val h = new HashTable[AnyRef, String]()

        h(Obj1) = "one"
        h(Obj2) = "two"
      }

      "both elements exist in the table" in new Fixture {
        assert(h.size === 2)
        assert(h(Obj1) === "one")
        assert(h(Obj2) === "two")
      }

      "removing one element doesn't affect the other" in new Fixture {
        h -= Obj1
        assert(h.size === 1)
        assert(h.get(Obj1).isEmpty)
        assert(h(Obj2) === "two")
      }
    }
  }
}
