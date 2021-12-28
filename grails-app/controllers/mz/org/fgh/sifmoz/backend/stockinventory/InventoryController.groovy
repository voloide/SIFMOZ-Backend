package mz.org.fgh.sifmoz.backend.stockinventory

import grails.converters.JSON
import grails.rest.RestfulController
import grails.validation.ValidationException
import mz.org.fgh.sifmoz.backend.utilities.JSONSerializer

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.NO_CONTENT
import static org.springframework.http.HttpStatus.OK

import grails.gorm.transactions.ReadOnly
import grails.gorm.transactions.Transactional

class InventoryController extends RestfulController{

    IInventoryService inventoryService

    static responseFormats = ['json', 'xml']
    static allowedMethods = [startNewInventory: "POST", update: "PUT", delete: "DELETE", close: "PUT"]

    InventoryController() {
        super(Inventory)
    }

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        render JSONSerializer.setObjectListJsonResponse(inventoryService.list(params)) as JSON
    }

    def show(Long id) {
        render JSONSerializer.setJsonObjectResponse(inventoryService.get(id)) as JSON
    }

    @Transactional
    def close(Inventory inventory) {

        try {
            inventory.close();
            inventoryService.processInventoryAdjustments(inventory)
            inventoryService.save(inventory)
        } catch (ValidationException e) {
            respond inventory.errors
            return
        }

        respond inventory, [status: OK, view:"show"]
    }

    @Transactional
    def startNewInventory(Inventory inventory) {
        if (inventory == null) {
            render status: NOT_FOUND
            return
        }
        if (inventory.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond inventory.errors
            return
        }

        try {
            inventoryService.initInventory(inventory)
        } catch (ValidationException e) {
            respond inventory.errors
            return
        }

        respond inventory, [status: CREATED, view:"show"]
    }

    /*@Transactional
    def save(Inventory inventory) {
        if (inventory == null) {
            render status: NOT_FOUND
            return
        }
        if (inventory.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond inventory.errors
            return
        }

        try {
            inventoryService.save(inventory)
        } catch (ValidationException e) {
            respond inventory.errors
            return
        }

        respond inventory, [status: CREATED, view:"show"]
    }*/

    @Transactional
    def update(Inventory inventory) {
        if (inventory == null) {
            render status: NOT_FOUND
            return
        }
        if (inventory.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond inventory.errors
            return
        }

        try {
            inventoryService.save(inventory)
        } catch (ValidationException e) {
            respond inventory.errors
            return
        }

        respond inventory, [status: OK, view:"show"]
    }

    @Transactional
    def delete(Long id) {
        if (id == null || inventoryService.delete(id) == null) {
            render status: NOT_FOUND
            return
        }

        render status: NO_CONTENT
    }
}
