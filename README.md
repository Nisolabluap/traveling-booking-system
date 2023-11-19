# Orange Team: Travel Booking System

## Description

Traveling Booking System is a comprehensive solution for managing travel packages and services offered by a travel
agency. This system streamlines the process of package bookings, payment processing, and itinerary management for both
travel agency staff and customers.

- [API Reference](#api-reference)
    * [Travel Package](#api-reference)
    * [Customer](#api-reference)
    * [Booking](#api-reference)
    * [Payment](#api-reference)

- [Features](#features)
- [Installation](#installation)
- [Authors](#authors)

## API Reference

#### Travel Package

| Parameter | Type     | 
| :-------- | :------- | 
| `name   ` | `string` | 
| `destination   ` | `string` | 
| `description   ` | `string` | 
| `pricePerPersonBeforeDiscount  ` | `double` | 
| `discountPercent   ` | `int` | 
| `startingDate   ` | `LocalDate` | 
| `endingDate  ` | `LocalDate` | 
| `availableReservations   ` | `int` | 

#### Get All Travel Packages

```http
  GET /api/travel-packages
```
#### Create a new travel package
```http
  POST/api/travel-packages
```
#### Update Travel Package
```http
  PUT/api/travel-packages/{id}
```
#### Delete a travel package by ID
```http
  DELETE/api/travel-packages/{id}
```
#### Get a travel package by ID
```http
  DELETE/api/travel-packages/{id}
```
#### Get Travel Packages Between Dates
```http
  GET/api/travel-packages/between-dates
```
#### Get Travel Packages With Price Between Two Values
```http
  GET/api/travel-packages/by-price-range
```
#### Get Travel Packages By Destination
```http
  GET/api/travel-packages/by-destination
```

#### Customer

| Parameter | Type     | 
| :-------- | :------- | 
| `firstName   ` | `string` | 
| `lastName   ` | `string` | 
| `phoneNumber   ` | `string` | 
| `email  ` | `string` | 

#### Create a new customer

```http
  POST /api/customers
```
#### Get all customers

```http
  GET /api/customers
```
#### Update a customer by ID

```http
  PUT /api/customers/{id}
```
#### Delete a customer by ID

```http
 DELETE /api/customers/{id}
```
#### Get a customer by ID

```http
GET/api/customers/{id}
```
### Booking

| Parameter | Type     | 
| :-------- | :------- | 
| `customerID   ` | `long` | 
| `travelPackageID   ` | `long` | 
| `numTravelers  ` | `int` |  

#### Get Booking By ID

```http
  GET/api/bookings/{id}
```
#### Get Bookings By Customer
```http
  GET/api/bookings/params = "customerID"
```
#### Get Booking By Travel Package ID
```http
  GET/api/bookings/by-travelPackage"
```
#### Get Booking By Destination
```http
  GET/api/bookings/by-destination"
```
#### Create Booking
```http
  POST/api/bookings
```
#### Update NumTravelers
```http
  POST/api/bookings/update-number-travelers/{id}
```
#### Cancel Booking
```http
  POST/api/bookings/cancel-booking/{id}
```
### Payment

| Parameter | Type     | 
| :-------- | :------- | 
| `paymentDateTime   ` | `LocalDateTime` | 
| `amount  ` | `double` | 
| `bookingID  ` | `long` | 
| `bankAccountInfo   ` | `string` | 

#### Process Payment

```http
  POST/api/payments
```
## Features

- **Online Reservations:** Users can make online reservations for accommodation.
- **Travel Management:** Interface for managing travel details and reservations.
- **Notifications:** Notification system for booking confirmations and updates.



## Installation

Install Travel Booking System

```
 - Clone this repository: `git clone https://github.com/Nisolabluap/traveling-booking-system.git`
```
## Authors
- [Paul Baloșin](https://github.com/Nisolabluap)
- [Adrian Pușcuță](https://github.com/Adrianpush)
- [Olteanu Alexandra](https://github.com/Alexandra10244)
- [Fernando Constantin Stinga](https://github.com/FernoCosti)
