// Deprecated: keeping this file for backward-compatibility only. The application now uses JPA
// single-table inheritance (discriminator column `dtype`) and type checks (instanceof)
// to determine whether a given User is a StudentWorker or ManagerUser.

package se300.shiftlift;

@Deprecated
public @interface Admin {
}
