package mz.org.fgh.sifmoz.backend.patientIdentifier

import grails.gorm.services.Service

@Service(PatientProgramIdentifier)
interface PatientIdentifierService {

    PatientProgramIdentifier get(Serializable id)

    List<PatientProgramIdentifier> list(Map args)

    Long count()

    PatientProgramIdentifier delete(Serializable id)

    PatientProgramIdentifier save(PatientProgramIdentifier patientIdentifier)

}
