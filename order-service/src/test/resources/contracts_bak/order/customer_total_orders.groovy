package contracts.order

import org.springframework.cloud.contract.spec.Contract

[
        Contract.make {
            description "Get customer total orders found"
            request {
                method 'GET'
                url("/v1/customer-total-orders") {
                    queryParameters {
                        parameter("customerID", "2")
                    }
                }
            }
            response {
                status 200
                body([
                        "totalOrders": 10
                ])
                headers {
                    contentType('application/json')
                }
            }
        },

        Contract.make {
            description "Get customer total orders not found"
            request {
                method 'GET'
                url("/v1/customer-total-orders") {
                    queryParameters {
                        parameter("customerID", "200")
                    }
                }
            }
            response {
                status 404
                body([
                        "message": "Not found any record with given customerID"
                ])
                headers {
                    contentType('application/json')
                }
            }
        }

]

