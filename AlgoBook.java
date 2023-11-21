import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
You are working for a promising new social media company's internal budget tracking tool.
The CEO would like to be able to input any individual at the company and figure out the budget of their organization

The organization hierarchy is modeled as a tree where:
- individual contributors (IC) are leaf nodes
- organization managers (M) are internal nodes.

When figuring out an employee's organization there are two cases to consider:
1. If the employee is an M (internal node), their organization is the subtree rooted at their corresponding node.
2. If an employee is an IC (leaf node), their organization is the subtree rooted at their manager's corresponding node.
An organization’s budget is the sum of the budgets of every person in the organization (M and IC).

A node in the organization tree should at least have:
- The employee id
- The budget of that employee
- A list of direct reports
- The employee's manager

This program has two main components:
1. A constructor for the organization class which takes in a list of Employee objects.
 The constructor must convert the list into a tree data structure and store it as a class field.
 Each employee object has the following:
   employeeId
   managerId (or null if it is the CEO/root)
   budget.
 Hint: it will make things easier if you also store a mapping of employeeId to EmployeeTreeNodes as a second class field.
 This will help for the second requirement.

2. The get_org_budget method, which takes in an employee id and returns the total budget of that employee's organization.
 You must use a Tree data structure to store the organization and a tree traversal to get an organization’s budget.

You are given the following in the starter code:
- The stubbed Organization class
- An Employee class
- An EmployeeTreeNode class with suggested fields
- Unit tests

You may modify the EmployeeTreeNode class but may not change the Employee class.

You must use a Tree data structure to store the organization and a tree traversal to get an organization’s budget.

You must also provide an explanation of the running time of the getOrgBudget method

Code Author: <Hal Jones>

Running Time Analysis of getOrgBudget

The running time analysis of the method getOrgBudget will be O(n). In this case, "n" will be the number of nodes, or employees, in our Employee Org Tree.
Because my getOrgBudget uses the "calculateBudget" method, a recursive function is used and taverses my employees/nodes and calcluates the budgets in a 
given subtree. It calculates it node by node, and therefore the time function
of Big O will be O(N), or O(n).

--------------------
<Your analysis here>
**/
public class AlgoBook {
	public static class Employee {
		private final int employeeId;
		private final Integer managerId;
		private final int budget;

		public Employee(int employeeId, Integer managerId, int budget) {
			this.employeeId = employeeId;
			this.managerId = managerId;
			this.budget = budget;
		}

		public int getEmployeeId() {
			return employeeId;
		}

		public Integer getManagerId() {
			return managerId;
		}

		public int getBudget() {
			return budget;
		}
	}

	private static class EmployeeTreeNode {
		private int id;
		private int budget;
		private EmployeeTreeNode managerNode;
		private Integer managerId;
		private List<EmployeeTreeNode> reports;
		private boolean isManager;

		public EmployeeTreeNode(Employee employee) {
			this.id = employee.getEmployeeId();
			this.budget = employee.getBudget();
			this.managerId = employee.getManagerId();
			// The below fields need to be set after instantiation
			this.reports = new ArrayList<>();
			this.isManager = false;
			this.managerNode = null;
		}

		public int getBudget() {
			return budget;
		}

		public EmployeeTreeNode getManagerNode() {
			return managerNode;
		}

		public void setManagerNode(EmployeeTreeNode managerNode) {
			this.managerNode = managerNode;
		}

		public Integer getManagerId() {
			return managerId;
		}

		public List<EmployeeTreeNode> getReports() {
			return reports;
		}

		public void addReport(EmployeeTreeNode report) {
			this.reports.add(report);
		}

		public boolean isManager() {
			return isManager;
		}

		public void setIsManager(boolean manager) {
			isManager = manager;
		}
	}

	private static class AlgoBookOrganization {

		// Create a map to hold the employees and the new EmployeeTreeNodes
		private Map<Integer, EmployeeTreeNode> employeeTreeNodeMap = new HashMap<>();

		public AlgoBookOrganization(List<Employee> employees) {

			// Instantiate the EmployeeTreeNodes using the data in the array list
			for (int i = 0; i < employees.size(); i++) {
				Employee employee = employees.get(i);
				EmployeeTreeNode employeeNode = new EmployeeTreeNode(employee);
				// Add them to your map. employeeNode is the value. Key is employeeId.
				employeeTreeNodeMap.put(employee.getEmployeeId(), employeeNode);
			}
			// Assign managers to each employee and list direct reports for each manager
			for (EmployeeTreeNode node : employeeTreeNodeMap.values()) {
				Integer managerId = node.getManagerId();
				if (managerId != null) {
					EmployeeTreeNode managerNode = employeeTreeNodeMap.get(managerId);
					if (managerNode != null) {
						managerNode.addReport(node);
						node.setManagerNode(managerNode);
					}
				}
			}
		}

		// getOrgBudget Method - Parameter employeeId
		public int getOrgBudget(int employeeId) {

			EmployeeTreeNode rootNode = employeeTreeNodeMap.get(employeeId);

			if (rootNode == null) {
				return 0;
			}

			return calculateBudget(rootNode);
		}

		// Helper method - Paramter EmployeeTreeNode

		private int calculateBudget(EmployeeTreeNode node) {
			int totalBudget = node.getBudget();

			for (EmployeeTreeNode report : node.getReports()) {
				totalBudget += calculateBudget(report);
			}
			return totalBudget;
		}

	}

	/*
	 * DO NOT EDIT BELOW THIS Below is the unit testing suite for this file. It
	 * provides all the tests that your code must pass to get full credit.
	 */
	public static void runUnitTests() {
        testSimpleCeo();
        testSimpleIndividual();
        testSimpleManager();
        testEmployeeNotFound();
        testManyReports();
        testEntireOrg();
    }

	private static void printTestResult(String testName, boolean result) {
        String color = result ? "\033[92m" : "\033[91m";
        String reset = "\033[0m";
        System.out.println(color + "[" + result + "] " + testName + reset);
    }

	private static void testAnswer(String testName, int result, int expected) {
        if (result == expected) {
            printTestResult(testName, true);
        } else {
            printTestResult(testName, false);
            System.out.println("Expected: " + expected + "\nGot:      " + result);
        }
    }

	private static void testAnswer(String testName, int[] result, int[] expected) {
        if (Arrays.equals(result, expected)) {
            printTestResult(testName, true);
        } else {
            printTestResult(testName, false);
            System.out.println("Expected: " + Arrays.toString(expected) + "\nGot:      " + Arrays.toString(result));
        }
    }

	private static void testSimpleCeo() {
        AlgoBookOrganization org = new AlgoBookOrganization(Arrays.asList(
                new Employee(123, null, 1000),
                new Employee(233, 123, 1000),
                new Employee(301, 233, 100),
                new Employee(302, 233, 200),
                new Employee(305, 234, 1000),
                new Employee(304, 234, 0),
                new Employee(303, 234, 0),
                new Employee(234, 123, 2000),
                new Employee(235, 123, 3000)
        ));
        int result = org.getOrgBudget(123);
        int expected = 8300;

        testAnswer("testSimpleCeo", result, expected);
    }

	private static void testSimpleIndividual() {
        AlgoBookOrganization org = new AlgoBookOrganization(Arrays.asList(
                new Employee(123, null, 1000),
                new Employee(233, 123, 1000),
                new Employee(301, 233, 100),
                new Employee(302, 233, 200),
                new Employee(305, 234, 1000),
                new Employee(304, 234, 0),
                new Employee(303, 234, 0),
                new Employee(234, 123, 2000),
                new Employee(235, 123, 3000)
        ));
        int result = org.getOrgBudget(235);
        int expected = 8300;

        testAnswer("testSimpleIndividual", result, expected);
    }

	private static void testSimpleManager() {
        AlgoBookOrganization org = new AlgoBookOrganization(Arrays.asList(
                new Employee(123, null, 1000),
                new Employee(233, 123, 1000),
                new Employee(301, 233, 100),
                new Employee(302, 233, 200),
                new Employee(305, 234, 1000),
                new Employee(304, 234, 0),
                new Employee(303, 234, 0),
                new Employee(234, 123, 2000),
                new Employee(235, 123, 3000)
        ));
        int result = org.getOrgBudget(233);
        int expected = 1300;

        testAnswer("testSimpleManager", result, expected);
    }

	private static void testEmployeeNotFound() {
        AlgoBookOrganization org = new AlgoBookOrganization(Arrays.asList(
                new Employee(123, null, 1000),
                new Employee(233, 123, 1000),
                new Employee(301, 233, 100),
                new Employee(302, 233, 200),
                new Employee(305, 234, 1000),
                new Employee(304, 234, 0),
                new Employee(303, 234, 0),
                new Employee(234, 123, 2000),
                new Employee(235, 123, 3000)
        ));
        int result = org.getOrgBudget(404);
        int expected = 0;

        testAnswer("testEmployeeNotFound", result, expected);
    }

	private static void testManyReports() {
        AlgoBookOrganization org = new AlgoBookOrganization(Arrays.asList(
                new Employee(123, null, 1000),
                new Employee(233, 123, 1000),
                new Employee(301, 233, 50),
                new Employee(302, 233, 50),
                new Employee(305, 233, 1000),
                new Employee(304, 233, 0),
                new Employee(303, 233, 0),
                new Employee(234, 233, 2000),
                new Employee(235, 233, 3000)
        ));
        int result = org.getOrgBudget(233);
        int expected = 7100;

        testAnswer("testManyReports", result, expected);
    }

	private static void testEntireOrg() {
        AlgoBookOrganization org = new AlgoBookOrganization(Arrays.asList(
                new Employee(1, null, 1000),  // CEO
                new Employee(2, 1, 500),  // Manager 1
                new Employee(3, 1, 600),  // Manager 2
                new Employee(4, 2, 200),  // Manager 3
                new Employee(5, 2, 300),  // Employee 1
                new Employee(6, 3, 400),  // Employee 2
                new Employee(7, 3, 200),  // Employee 3
                new Employee(8, 4, 100),  // Employee 4
                new Employee(9, 4, 100),  // Employee 5
                new Employee(10, 4, 100)  // Employee 6
        ));
        int[] results = new int[10];
        int[] expected = { 3500, 1300, 1200, 500, 1300, 1200, 1200, 500, 500, 500 };

        for (int i = 0; i < 10; i++) {
            results[i] = org.getOrgBudget(i + 1);
        }

        testAnswer("testEntireOrg", results, expected);
    }

	public static void main(String[] args) {
        runUnitTests();
    }
}
