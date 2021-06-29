package mz.org.fgh.sifmoz.backend.patient

import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.NO_CONTENT
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY

import grails.gorm.transactions.ReadOnly
import grails.gorm.transactions.Transactional

@ReadOnly
class PatientIdentifierController {

    PatientIdentifierService patientIdentifierService

    static responseFormats = ['json', 'xml']
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond patientIdentifierService.list(params), model:[patientIdentifierCount: patientIdentifierService.count()]
    }

    def show(Long id) {
        respond patientIdentifierService.get(id)
    }

    @Transactional
    def save(PatientIdentifier patientIdentifier) {
        if (patientIdentifier == null) {
            render status: NOT_FOUND
            return
        }
        if (patientIdentifier.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond patientIdentifier.errors
            return
        }

        try {
            patientIdentifierService.save(patientIdentifier)
        } catch (ValidationException e) {
            respond patientIdentifier.errors
            return
        }

        respond patientIdentifier, [status: CREATED, view:"show"]
    }

    @Transactional
    def update(PatientIdentifier patientIdentifier) {
        if (patientIdentifier == null) {
            render status: NOT_FOUND
            return
        }
        if (patientIdentifier.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond patientIdentifier.errors
            return
        }

        try {
            patientIdentifierService.save(patientIdentifier)
        } catch (ValidationException e) {
            respond patientIdentifier.errors
            return
        }

        respond patientIdentifier, [status: OK, view:"show"]
    }

    @Transactional
    def delete(Long id) {
        if (id == null || patientIdentifierService.delete(id) == null) {
            render status: NOT_FOUND
            return
        }

        render status: NO_CONTENT
    }
}