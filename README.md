# Property Booking API

I've built this API to handle property bookings and blocks with a focus on reliability, security, and performance.

## Important Facts

- The configurable booking window (limiting how far in advance users can book) not only prevents unreasonable bookings for the year 3001, but also significantly optimizes our overlap calculations.
- With a 2-year booking window, the maximum number of active bookings per property is capped at 731 days.
- Combined with our archiving strategy and indexing, this keeps our availability checks lightning fast even at scale.

## API : What it does

### Booking Management
- Full CRUD operations for bookings
- Ability to cancel and rebook as needed
- Nightly job archives old bookings automatically

### Block Management
- Owners can block dates on their properties
- Complete CRUD operations for blocks
- Automatic archiving of old blocks

### Security
- Role-based access (guests, owners, managers)
- Ownership checks on all resources
- JWT-based authentication

### Data Validation
- Custom validators ensure data integrity
- Prevents invalid date ranges (end before start)
- Blocks double-bookings through availability checks
- Clean DTO/entity mapping with MapStruct( plus extra validation)
- Configurable booking window (e.g., only allow bookings up to 2 years in advance)

### Concurrency Handling
- Property-level locks prevent booking conflicts(can easily moved to a distributed lock to scale)
- Handles race conditions when multiple users book simultaneously

### Performance
- Optimized DB queries for availability checks: indexes and usage of EXISTS queries for early termination
- tested locally on 5 million booking is less than 2ms
### Business Logic
- Prevents updates to archived/canceled bookings
- Sophisticated overlap detection between bookings and blocks

## Testing

I've written extensive integration tests covering the main scenarios you'll encounter. They're a good reference for how the API behaves in different situations.
