package cscie56.ps3
import cscie56.ps2.Game
import cscie56.ps2.Team

import grails.test.mixin.*
import spock.lang.*

@TestFor(GameStatsController)
@Mock(GameStats)
class GameStatsControllerSpec extends Specification {

    def populateValidParams(params) {
        assert params != null


        params << [player: new Person(), minutesPlayed: 48,
                points: 30, assists: 8,
                rebounds: 9, steals: 3,
                shotsAttempted: 30,
                shotsMade: 14,
                shootingPercentage: 56.5,
                threePointersAttempted: 2,
                threePointersMade: 2,
                threePointPercentage: 100.0,
                personalFouls: 2]
    }

    void "Test the index action returns the correct model"() {

        when:"The index action is executed"
            controller.index()

        then:"The model is correct"
            !model.gameStatsList
            model.gameStatsCount == 0
    }

    void "Test the create action returns the correct model"() {
        when:"The create action is executed"
            controller.create()

        then:"The model is correctly created"
            model.gameStats!= null
    }

    void "Test the save action correctly persists an instance"() {

        when:"The save action is executed with an invalid instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'POST'
            def gameStats = new GameStats()
            gameStats.validate()
            controller.save(gameStats)

        then:"The create view is rendered again with the correct model"
            model.gameStats!= null
            view == 'create'

        when:"The save action is executed with a valid instance"
            response.reset()
            populateValidParams(params)
            gameStats = new GameStats(params)

            controller.save(gameStats)

        then:"A redirect is issued to the show action"
            response.redirectedUrl == '/gameStats/show/1'
            controller.flash.message != null
            GameStats.count() == 1
    }

    void "Test that the show action returns the correct model"() {
        when:"The show action is executed with a null domain"
            controller.show(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the show action"
            populateValidParams(params)
            def gameStats = new GameStats(params)
            controller.show(gameStats)

        then:"A model is populated containing the domain instance"
            model.gameStats == gameStats
    }

    void "Test that the edit action returns the correct model"() {
        when:"The edit action is executed with a null domain"
            controller.edit(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the edit action"
            populateValidParams(params)
            def gameStats = new GameStats(params)
            controller.edit(gameStats)

        then:"A model is populated containing the domain instance"
            model.gameStats == gameStats
    }

    void "Test the update action performs an update on a valid domain instance"() {
        when:"Update is called for a domain instance that doesn't exist"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'PUT'
            controller.update(null)

        then:"A 404 error is returned"
            response.redirectedUrl == '/gameStats/index'
            flash.message != null

        when:"An invalid domain instance is passed to the update action"
            response.reset()
            def gameStats = new GameStats()
            gameStats.validate()
            controller.update(gameStats)

        then:"The edit view is rendered again with the invalid instance"
            view == 'edit'
            model.gameStats == gameStats

        when:"A valid domain instance is passed to the update action"
            response.reset()
            populateValidParams(params)
            gameStats = new GameStats(params).save(flush: true)
            controller.update(gameStats)

        then:"A redirect is issued to the show action"
            gameStats != null
            response.redirectedUrl == "/gameStats/show/$gameStats.id"
            flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when:"The delete action is called for a null instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            controller.delete(null)

        then:"A 404 is returned"
            response.redirectedUrl == '/gameStats/index'
            flash.message != null

        when:"A domain instance is created"
            response.reset()
            populateValidParams(params)
            def gameStats = new GameStats(params).save(flush: true)

        then:"It exists"
            GameStats.count() == 1

        when:"The domain instance is passed to the delete action"
            controller.delete(gameStats)

        then:"The instance is deleted"
            GameStats.count() == 0
            response.redirectedUrl == '/gameStats/index'
            flash.message != null
    }
}
