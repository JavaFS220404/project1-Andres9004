package com.revature.models;

/**
 * Reimbursements within the ERS application can be of the following types:
 * <ul>
 *     <li>Pending</li>
 *     <li>Approved</li>
 *     <li>Denied</li>
 * </ul>
 */
public enum RType {

    LODGING {
        @Override
        public String toString() {
            return "LODGING";
        }
    },
    TRAVEL {
        @Override
        public String toString() {
            return "TRAVEL";
        }
    },
    FOOD {
        @Override
        public String toString() {
            return "FOOD";
        }
    },
    OTHER {
        @Override
        public String toString() {
            return "OTHER";
        }
    }
}
