package mz.org.fgh.sifmoz.backend.patient

import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import org.grails.datastore.mapping.core.Datastore
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification

@Integration
@Rollback
class PatientIdentifierServiceSpec extends Specification {

    PatientIdentifierService patientIdentifierService
    @Autowired Datastore datastore

    private Long setupData() {
        // TODO: Populate valid domain instances and return a valid ID
        //new PatientIdentifier(...).save(flush: true, failOnError: true)
        //new PatientIdentifier(...).save(flush: true, failOnError: true)
        //PatientIdentifier patientIdentifier = new PatientIdentifier(...).save(flush: true, failOnError: true)
        //new PatientIdentifier(...).save(flush: true, failOnError: true)
        //new PatientIdentifier(...).save(flush: true, failOnError: true)
        assert false, "TODO: Provide a setupData() implementation for this generated test suite"
        //patientIdentifier.id
    }

    void cleanup() {
        assert false, "TODO: Provide a cleanup implementation if using MongoDB"
    }

    void "test get"() {
        setupData()

        expect:
        patientIdentifierService.get(1) != null
    }

    void "test list"() {
        setupData()

        when:
        List<PatientIdentifier> patientIdentifierList = patientIdentifierService.list(max: 2, offset: 2)

        then:
        patientIdentifierList.size() == 2
        assert false, "TODO: Verify the correct instances are returned"
    }

    void "test count"() {
        setupData()

        expect:
        patientIdentifierService.count() == 5
    }

    void "test delete"() {
        Long patientIdentifierId = setupData()

        expect:
        patientIdentifierService.count() == 5

        when:
        patientIdentifierService.delete(patientIdentifierId)
        datastore.currentSession.flush()

        then:
        patientIdentifierService.count() == 4
    }

    void "test save"() {
        when:
        assert false, "TODO: Provide a valid instance to save"
        PatientIdentifier patientIdentifier = new PatientIdentifier()
        patientIdentifierService.save(patientIdentifier)

        then:
        patientIdentifier.id != null
    }
}