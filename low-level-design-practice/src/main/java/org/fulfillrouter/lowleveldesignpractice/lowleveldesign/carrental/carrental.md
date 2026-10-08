**Car Rental System Design 
```text
Car Rental
│
├── Find Available Cars
│   → filter by location, car type, pickup time, return time
│
├── Make Reservation
│   → who rents which car
│   → pickup time / return time
│   → reservation status
│
├── Payment
│   → how much to pay
│   → which payment processor to use
│   → create payment transaction
│
├── Payment Attempt
│   → process the payment
│   → update payment status
│   → record who / when / which reservation
│
└── Cancel Reservation
    → update reservation status
    → update payment status
    → refund if necessary
```

Then the overall state flow becomes:

```text
Find Available Cars
        ↓
Make Reservation
        ↓
Create Payment
        ↓
Payment Attempt
        ↓
Success / Failed
        ↓
Confirmed / Failed Reservation

Cancel Reservation
        ↓
Update Reservation
        ↓
Refund / Update Payment
```
