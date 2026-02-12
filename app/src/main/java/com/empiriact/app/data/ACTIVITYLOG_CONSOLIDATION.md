# ActivityLog Migration & Consolidation

## Problem Statement
There were multiple conflicting definitions of ActivityLog-related classes across different packages:
- `ActivityLog` (old definition in `/data`)
- `ActivityLogEntity` (new definition in `/db`)
- `ActivityLogRepository` (implementations in both `/data` and `/repo`)
- `ActivityLogDao` (implementation in `/db` using wrong entity type)

This caused:
1. Type mismatches (Long vs Int parameters)
2. Schema conflicts (different column names)
3. Import resolution errors
4. Compilation failures

## Solution Implemented

### Single Source of Truth Pattern
All database-related classes are now defined once in the appropriate package:

```
/data/db/
├── ActivityLogEntity.kt      ← Single definition (timestamp, activity, rating → key, localDate, hour)
├── ActivityLogDao.kt         ← Updated to use ActivityLogEntity with correct query parameters
└── README.md                 ← Database layer documentation

/data/repo/
├── ActivityLogRepository.kt  ← Uses ActivityLogEntity with proper type conversions
└── [other repositories]

/data/
├── ActivityLog.kt            ← Re-export typealias → ActivityLogEntity (backward compatibility)
├── ActivityLogDao.kt         ← Re-export typealias → db.ActivityLogDao (backward compatibility)
└── ActivityLogRepository.kt  ← Re-export typealias → repo.ActivityLogRepository (backward compatibility)
```

### Changes Made

#### 1. **ActivityLogEntity.kt** (/db) - Single Definition
```kotlin
@Entity(tableName = "activity_logs")
data class ActivityLogEntity(
    @PrimaryKey
    val key: String,              // "$localDate-$hour"
    val localDate: Int,           // yyyyMMdd
    val hour: Int,                // 0..23
    val activityText: String,
    val valence: Int,             // -2..2
    val updatedAtEpochMs: Long
)
```

**Why**: New schema is more flexible for hourly logging with date-based queries

#### 2. **ActivityLogDao.kt** (/db) - Fixed Type Consistency
```kotlin
// BEFORE: Mixed Long and ActivityLog types
fun observeDay(timestamp: Long): Flow<List<ActivityLog>>

// AFTER: Consistent Int (yyyyMMdd) and ActivityLogEntity
fun observeDay(dateInt: Int): Flow<List<ActivityLogEntity>>
```

**Why**: Matches ActivityLogEntity schema and fixes parameter type mismatches

#### 3. **ActivityLogRepository.kt** (/repo) - Enriched Implementation
- Added missing methods: `getLogsForDay`, `getLogsForWeek`, `getLogsForMonth`
- Fixed parameter types: `Int` instead of `Long`
- Added `YearMonth` convenience overload
- Added `deleteDay` method

```kotlin
fun getLogsForDay(startDate: LocalDate, endDate: LocalDate): Flow<List<ActivityLogEntity>>
fun getLogsForMonth(yearMonth: YearMonth): Flow<List<ActivityLogEntity>>
```

**Why**: Complete API for all time-based queries, type-safe

#### 4. **Backward Compatibility Re-exports** (/data)
- `ActivityLog.kt` → typealias to `ActivityLogEntity`
- `ActivityLogDao.kt` → typealias to `db.ActivityLogDao`
- `ActivityLogRepository.kt` → typealias to `repo.ActivityLogRepository`

**Why**: Old code using old imports continues to work, enabling gradual migration

## Migration Path for Existing Code

### Old Code (Still Works)
```kotlin
import com.empiriact.app.data.ActivityLog
import com.empiriact.app.data.ActivityLogRepository

// Still works because of re-export typealiases
```

### New Code (Recommended)
```kotlin
import com.empiriact.app.data.db.ActivityLogEntity
import com.empiriact.app.data.db.ActivityLogDao
import com.empiriact.app.data.repo.ActivityLogRepository

// Direct imports from the source of truth
```

## Key Improvements

✅ **Single Source of Truth**: One definition per class
✅ **Type Safety**: Consistent parameter types throughout
✅ **Backward Compatibility**: Old imports work via re-export
✅ **API Completeness**: All time-based query methods present
✅ **Date Handling**: YYYYMMDD format (Int) is more efficient than Long timestamps
✅ **Extensibility**: Clear structure for future enhancements

## Future Improvements

1. Complete migration of all code to use new imports
2. Remove old entity schema from database version tracking
3. Database migration to consolidate old and new formats
4. Remove re-export typealiases once migration is complete

## Testing Checklist

- [ ] Compilation succeeds without errors
- [ ] KSP processing completes
- [ ] DAOs generate correct code
- [ ] Repository methods work correctly
- [ ] Backward compatibility imports resolve
- [ ] Date conversion functions work properly
