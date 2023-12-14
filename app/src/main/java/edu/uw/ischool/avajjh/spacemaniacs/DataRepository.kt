package edu.uw.ischool.avajjh.spacemaniacs

interface dataRepoInterface {
    fun updateEvents(array: Array<Event>)
    fun updateLaunches(array: Array<Launch>)
    fun updateAstronauts(array: Array<Astronaut>)
    fun getEvents(): Array<Event>
    fun getLaunches(): Array<Launch>
    fun getAstronauts(): Array<Astronaut>
}
class DataRepository() : dataRepoInterface {
    lateinit var eventArray: Array<Event>
    var astronautArray: Array<Astronaut> = emptyArray()
    lateinit var launchArray: Array<Launch>
    
    override fun updateEvents(array: Array<Event>) {
        eventArray = array
    }

    override fun updateLaunches(array: Array<Launch>) {
        launchArray = array
    }

    override fun updateAstronauts(array: Array<Astronaut>) {
        astronautArray = array
    }

    override fun getEvents(): Array<Event> {
        return eventArray
    }

    override fun getLaunches(): Array<Launch> {
        return launchArray
    }

    override fun getAstronauts(): Array<Astronaut> {
        return astronautArray
    }

}