package org.overbeck.scraper

import java.util.{Calendar, TimeZone}

import org.jsoup.Jsoup
import ujson.{Arr, Obj}

import scala.jdk.CollectionConverters._


object Scraper {

  def availableScrapes() = {
    Arr(
      Obj("style" -> "wide", "name" -> "adamathome")
    )
  }

  def item(item: String) = {
    val now = Calendar.getInstance(TimeZone.getTimeZone("America/Los Angeles"))
    val url = s"https://www.gocomics.com/adamathome/${now.get(Calendar.YEAR)}/${now.get(Calendar.MONTH) + 1}/${now.get(Calendar.DAY_OF_MONTH)}"
    val doc = Jsoup.connect(url).get()
    val elements = doc.select("div[data-image^=https]")
    if (elements.isEmpty == 0) None
    else elements.get(0).attributes().dataset().asScala.get("image")
  }

  /*
      'adam': 'adamathome',
    'calvin': 'calvinandhobbes',
    'duplex': 'duplex',
    'nancy': 'nancy',
    'pooch': 'poochcafe'

   */




}
