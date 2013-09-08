package com.dennishorte

/*
 * Simple implementation of a HashTable backed by an array.
 * Main class written in approximately 20 minutes.
 * 
 * The purpose of this is not be production ready code, but to let me explore the
 * implementation of HashTables.
 *
 * Whenever the map already has max_size / 2 elements, the array is resized and all
 * existing elements are reassigned into the new array. When resized, the array size
 * is doubled.
 *
 * Many people choose poor hash functions which cause extra collisions when modded by
 * a power of 2, so the base size of the HashTable is set to 15.
 */
class HashTable[Key,Value](size_hint: Int = 15) {

  private var max_size = size_hint max 5

  private var elems = Array.fill(max_size){ List.empty[(Key,Value)] }
  private var count = 0

  protected def hash_value(key: Key): Int = {
    key.hashCode
  }

  private def get_index(key: Key): Int = {
    (hash_value(key) % max_size).abs
  }

  private def resize_array {
    val old = elems
    max_size = max_size * 2
    elems = Array.fill(max_size){ List.empty[(Key,Value)] }
    old.foreach{ list =>
      list.foreach{ case (key, value) => update(key, value) }
    }
  }

  /*
   * Recursively traverse the @param list of pairs, and if a pair matches @param key,
   * replace the pair with (@param key, @param value). If @param key is not in @param list,
   * append (@param key, @param value) to the list.
   */
  private def insert(key: Key, value: Value, list: List[(Key,Value)]): List[(Key,Value)] = {
    if (list.isEmpty) {
      count += 1
      (key, value) :: Nil
    }
    else if (list.head._1 == key) (key, value) :: list.tail
    else list.head :: insert(key, value, list.tail)
  }

  private def delete(key: Key, list: List[(Key,Value)]): List[(Key,Value)] = {
    if (list.isEmpty) Nil
    else if (list.head._1 == key) {
      count -= 1
      list.tail
    }
    else list.head :: delete(key, list.tail)
  }

  def -=(key: Key) {
    remove(key)
  }

  def remove(key: Key) {
    elems(get_index(key)) = delete(key, elems(get_index(key)))
  }

  /*
   * Elements are stored inside a list in their assigned bucket.
   * For each key assigned to a bucket, there will be a single tuple in the list
   * whose first element is that key.
   */
  def update(key: Key, value: Value) {
    if(count >= max_size) {
      resize_array
    }
    elems(get_index(key)) = insert(key, value, elems(get_index(key)))
  }

  def +=(pair: (Key,Value)) {
    update(pair._1, pair._2)
  }

  def apply(key: Key): Value = {
    get(key).get
  }

  def get(key: Key): Option[Value] = {
    elems(get_index(key)).find(_._1 == key).map(_._2)
  }

  def size: Int = count

  def contains(key: Key): Boolean = {
    elems(get_index(key)).exists(_._1 == key)
  }
}
