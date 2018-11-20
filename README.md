# spring-web-jpa

## Introduction

- Demo project about spring-boot, spring-webmvc, spring-jpa and spring test
- Requirements:
    - Java: Oracle JDK 10
    
## Blueprint Design

System contains services:

### Customer service:
 
 - Show customer list
 - Show customer detail. In customer detail show order list of this customer.

### Order service:
 
 - Show order list.
 - Show order list by customerID.
 - Show order list by itemID.
  
### Category service:
 
 - Show product type list: Example: Laptop, Mouse, Keyboard
 - Get properties list of each product type: Color, Manufacture, Price, Brand, Screen size, Storage capacity, Processor Type. VGA Type, RAM size, Storage size, Mobile OS, SIM Type...
 
Example:    

```json
{ "product-type-list":["laptop", "mouse", "keyboard"] }
```

```json
{
"product-type-list":[
     {
        "name": "laptop",
        "id": 1,
        "properties": [
            {
                "name": "screen-szie",
                "id": 1,
                "value": [
                    "15.6",
                    "14",
                    "13.3",
                    "10.6"
                ]
            },
            {
                "name": "screen-resolution",
                "id": 2,
                "value": [
                    "1920x1080",
                    "1366x768",
                    "3480x2160",
                    "2048x1536"
                ]
            },
            {
                "name": "cpu-type",
                "id": 3,
                "value": [
                    "core-i3",
                    "core-i5",
                    "core-i7",
                    "core-i9"
                ]
            },
            {
                "name": "ram-capacity",
                "id": 4,
                "value":[
                    "4GB",
                    "8GB",
                    "16GB"
                ]
            }

        ]
    },   
    {
        "name": "mouse",
        "id": 2,
        "properties": [
            {
                "name": "mouse-type",
                "id": 1,
                "value": [
                    "office",
                    "blue-tooth",
                    "wireless",
                    "gaming"
                ]
            },
            {
                "name": "size",
                "id": 2,
                "value": [
                    "small",
                    "normal",
                    "big"
                ]
            },
            {
                "name": "dpi",
                "id": 3,
                "value":[
                    "1000DPI",
                    "2000DPI",
                    "4000DPI"
                ]
            }
        ]
    },
    {
        "name": "mobile-phone",
        "id": 3,
        "propeties": [
            {
                "name": "screen-size",
                "id": 1,
                "value": [
                    "4.0 inch",
                    "4.5 inch",
                    "5.0 inch",
                    "6.0 inch",
                    "6.5 inch"
                ]
            },
            {
                "name": "screen-resolution",
                "id": 2,
                "value": [
                    "Full HD",
                    "HD",
                    "4K"
                ]
            },
            {
                "name": "OS",
                "id": 3,
                "value":[
                    "IOS",
                    "Android 4.4",
                    "Android 5.0",
                    "Android 6.0",
                    "Android 7.0",
                    "Android 8.0"
                ]
            },
            {
                "name": "RAM-capacity",
                "id": 4,
                "value":[
                    "2GB",
                    "3GB",
                    "4GB",
                    "6GB"
                ]
            },
            {
                "name": "SIM-type",
                "id": 5,
                "value":[
                    "Single SIM",
                    "Dual SIM"
                ]
            },{
                "name": "Connect-type",
                "id": 6,
                "value":[
                    "2G-GPRS",
                    "3G-HSPA",
                    "4G-LTE"
                ]
            }
    
        ]
    }  
  ]
}
```

### Item service:
 
- Show item list.
- Show manufacture list
- Show item info. In item info show order list order this item.
- Show reviews about an item
- Given a query contains propeties: product-type, and product-properties-value list, find all items satisfy these properties. Example:
  - Find all items has product-type is: mobile-phone and product-properties is: Sim-type: DUAL-SIM, RAM-CAPACITY: 4GB
  - Find all items has product-type is: mobile-phone and product-properties is: OS: Android 7.0, screen-resolution: 4K

## Database design

- Customer service:
  - CUSTOMER table

- Order service:
  - ORDER table

- Item service:
  - ITEM table
  - REVIEW table

- Category service: 
  - PRODUCT-TYPE table
  - PRODUCT-TYPE-PROPERTIES table 
  - PRODUCT-TYPE-PROPERTIES-VALUE table
  
-> Reference database schema: http://www.erdiagrams.com/datamodel-online-shop-idef1x.jpg

## Benchmark performance


```bash
docker run --rm williamyeh/wrk -t4 -c100 -d500s -H 'Host: example.com' --latency --timeout 30s http://192.168.120.1:8060/api/customer/customers\?address\=Ha%20Noi
```