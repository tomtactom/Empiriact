# Database Layer Documentation

## Package Structure

This package contains all Room Database related code:
- **Entity Classes**: Entity definitions for Room database tables
- **DAOs**: Data Access Objects for database operations
- **Database**: Main database configuration class

## Important Notes

### Single Source of Truth Pattern
Entity classes are defined in this package (`com.empiriact.app.data.db`) as they are closely tied to the database schema.

Some entity classes are re-exported from `com.empiriact.app.data` package for backward compatibility using Kotlin typealiases. This ensures:
1. Single definition point (DRY principle)
2. Backward compatibility for existing imports
3. Prevents KSP compilation errors from duplicate definitions

### Re-exported Classes
The following classes are defined here but re-exported from the parent package:
- `ExerciseRatingEntity` - Re-exported as `com.empiriact.app.data.ExerciseRatingEntity`
- `ExerciseAverageRating` - Re-exported as `com.empiriact.app.data.ExerciseAverageRating`

**Usage guideline**: New code should import from `com.empiriact.app.data.db` package directly.

## Future Improvements

- [ ] Move all entity definitions to this package for consistency
- [ ] Remove old entity definitions from parent package
- [ ] Update all imports to point to this package

## Common Issues & Solutions

### KSP Error: "Entity is not in the database"
This error occurs when:
1. An entity is referenced in `@Database(entities = [...])` but not imported correctly
2. There are multiple definitions of the same entity with different packages
3. Entity class names conflict with other classes

**Solution**: Ensure all entities are imported from the same package. Use the pattern in this file.
