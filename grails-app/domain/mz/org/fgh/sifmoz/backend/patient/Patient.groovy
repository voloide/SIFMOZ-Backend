package mz.org.fgh.sifmoz.backend.patient


import mz.org.fgh.sifmoz.backend.appointment.Appointment
import mz.org.fgh.sifmoz.backend.clinic.Clinic
import mz.org.fgh.sifmoz.backend.distribuicaoAdministrativa.Province
import mz.org.fgh.sifmoz.backend.groupMember.GroupMember
import mz.org.fgh.sifmoz.backend.patientAttribute.PatientAttribute
import mz.org.fgh.sifmoz.backend.patientIdentifier.PatientServiceIdentifier

class Patient {
    String id
    String firstnames
    String lastname
    String gender
    Date dateofbirth
    String cellphone
    String alternativeCellphone
    String address
    String otherAddress
    boolean accountstatus
    Clinic clinic
    String uuid = UUID.randomUUID().toString()

    static belongsTo = [province: Province]
    static hasMany = [
            attributes: PatientAttribute,
            identifiers: PatientServiceIdentifier,
            appointments: Appointment,
            groups: GroupMember
    ]

    static mapping = {
        id generator: "uuid"
    }

    static constraints = {
        dateofbirth(nullable: true, blank: true, validator: { dateofbirth, urc ->
            return dateofbirth != null ? dateofbirth <= new Date() : null
        })
        cellphone(nullable: true, matches: /\d+/, maxSize: 12, minSize: 9)
        alternativeCellphone(nullable: true, matches: /\d+/, maxSize: 12, minSize: 9)
    }
}
