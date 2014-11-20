package com.srednal.experiments

case class Moon(name: String, planet: String)

object Moons {

  def apply(planet: String) = byPlanet(planet) map (Moon(_, planet))

  val byPlanet = Map(
    "Mercury" -> Nil,
    "Venus" -> Nil,
    "Earth" -> ("Moon" :: Nil),
    "Mars" -> ("Phobos" :: "Deimos" :: Nil),
    "Jupiter" -> ("Io" :: "Europa" :: "Ganymede" :: "Callisto" :: "Amalthea" :: "Himalia" :: "Elara" :: "Pasiphae" ::
      "Sinope" :: "Lysithea" :: "Carme" :: "Ananke" :: "Leda" :: "Thebe" :: "Adrastea" :: "Metis" :: "Callirrhoe" ::
      "Themisto" :: "Megaclite" :: "Taygete" :: "Chaldene" :: "Harpalyke" :: "Kalyke" :: "Iocaste" :: "Erinome" ::
      "Isonoe" :: "Praxidike" :: "Autonoe" :: "Thyone" :: "Hermippe" :: "Aitne" :: "Eurydome" :: "Euanthe" :: "Euporie" ::
      "Orthosie" :: "Sponde" :: "Kale" :: "Pasithee" :: "Hegemone" :: "Mneme" :: "Aoede" :: "Thelxinoe" :: "Arche" ::
      "Kallichore" :: "Helike" :: "Carpo" :: "Eukelade" :: "Cyllene" :: "Kore" :: "Herse" :: Nil),
    "Saturn" -> ("Mimas" :: "Enceladus" :: "Tethys" :: "Dione" :: "Rhea" :: "Titan" :: "Hyperion" :: "Iapetus" ::
      "Erriapus" :: "Phoebe" :: "Janus" :: "Epimetheus" :: "Helene" :: "Telesto" :: "Calypso" :: "Kiviuq" :: "Atlas" ::
      "Prometheus" :: "Pandora" :: "Pan" :: "Ymir" :: "Paaliaq" :: "Tarvos" :: "Ijiraq" :: "Suttungr" :: "Mundilfari" ::
      "Albiorix" :: "Skathi" :: "Siarnaq" :: "Thrymr" :: "Narvi" :: "Methone" :: "Pallene" :: "Polydeuces" :: "Daphnis" ::
      "Aegir" :: "Bebhionn" :: "Bergelmir" :: "Bestla" :: "Farbauti" :: "Fenrir" :: "Fornjot" :: "Hati" :: "Hyrrokkin" ::
      "Kari" :: "Loge" :: "Skoll" :: "Surtur" :: "Greip" :: "Jarnsaxa" :: "Tarqeq" :: "Anthe" :: "Aegaeon" :: Nil),
    "Uranus" -> ("Cordelia" :: "Ophelia" :: "Bianca" :: "Cressida" :: "Desdemona" :: "Juliet" :: "Portia" :: "Rosalind" ::
      "Mab" :: "Belinda" :: "Perdita" :: "Puck" :: "Cupid" :: "Miranda" :: "Francisco" :: "Ariel" :: "Umbriel" :: "Titania" ::
      "Oberon" :: "Caliban" :: "Stephano" :: "Trinculo" :: "Sycorax" :: "Margaret" :: "Prospero" :: "Setebos" :: "Ferdinand" :: Nil),
    "Neptune" -> ("Triton" :: "Nereid" :: "Naiad" :: "Thalassa" :: "Despina" :: "Galatea" :: "Larissa" :: "Proteus" ::
      "Halimede" :: "Psamathe" :: "Sao" :: "Laomedeia" :: "Neso" :: Nil)
  )

  val moons = byPlanet.map {
    case (p, m) => m map (Moon(_, p))
  }.flatten.toList
}
