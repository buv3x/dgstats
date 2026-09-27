# basket-statistics-export Specification Delta

## ADDED Requirements

### Requirement: Public ordering metadata export

The statistics export SHALL expose nullable basket and basket-variation
`sortOrder` values needed by the static `docs/` page, while preserving all
existing exported statistics and player-identity boundaries.

#### Scenario: Basket sort order is exported

- **WHEN** a mapped rated score sample belongs to a basket with a non-null
  `sort_order`
- **THEN** the relevant public export record exposes that basket sort order

#### Scenario: Basket variation sort order is exported

- **WHEN** a mapped rated score sample belongs to a basket variation with a
  non-null `sort_order`
- **THEN** the relevant public export record exposes that variation sort order

#### Scenario: Missing sort order remains optional

- **WHEN** a basket or basket variation has a null `sort_order`
- **THEN** the export omits or serializes that value as null without failing
- **AND** the static page can apply its ID fallback ordering

#### Scenario: Player identity boundary is preserved

- **WHEN** sort-order metadata is exported
- **THEN** no additional player-identifying fields are added to course, Basket
  stats, or Personal stats records

### Requirement: Public course result count

The statistics manifest SHALL provide the total exported result count per course
for public selector ordering.

#### Scenario: Course result count is available

- **WHEN** the statistics manifest lists an included course
- **THEN** it includes `sampleCount` equal to the total rated, mapped exported
  samples for that course

#### Scenario: Course result count is not displayed by export labels

- **WHEN** a public course selector uses manifest course options
- **THEN** the exported course name remains independent of `sampleCount`
