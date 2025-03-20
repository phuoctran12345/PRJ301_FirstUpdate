<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@include file="../../common/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Revenue Statistics</title>
    <meta content="width=device-width, initial-scale=1.0" name="viewport">
    
    <!-- CSS Libraries -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/boxicons@latest/css/boxicons.min.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.2/css/all.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jquery-confirm/3.3.2/jquery-confirm.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="view/assets/admin/css/main.css">
</head>
<body onload="time()" class="app sidebar-mini rtl">
    <%@include file="../../common/admin/sidebar.jsp"%>

    <main class="app-content">
        <div class="app-title">
            <ul class="app-breadcrumb breadcrumb side">
                <li class="breadcrumb-item active"><a href="#"><b>Revenue Charts</b></a></li>
            </ul>
            <div id="clock"></div>
        </div>

        <c:if test="${not empty ERROR_MESSAGE}">
            <div class="alert alert-danger">${ERROR_MESSAGE}</div>
        </c:if>

        <div class="row">
            <div class="col-md-12">
                <div class="tile">
                    <div class="tile-body">
                        <div class="row">
                            <!-- Monthly Revenue Chart -->
                            <div class="col-sm-12 col-xl-6 mb-4">
                                <div class="bg-light text-center rounded p-4">
                                    <h6 class="mb-4">Monthly Revenue (Current Year)</h6>
                                    <canvas id="monthly-revenue"></canvas>
                                </div>
                            </div>

                            <!-- Yearly Revenue Chart -->
                            <div class="col-sm-12 col-xl-6 mb-4">
                                <div class="bg-light text-center rounded p-4">
                                    <h6 class="mb-4">Yearly Revenue (2018-2024)</h6>
                                    <canvas id="yearly-revenue"></canvas>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </main>

    <!-- JavaScript Libraries -->
    <script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    

    <script type="text/javascript">
        // Monthly Revenue Chart
        var ctx1 = document.getElementById("monthly-revenue").getContext("2d");
        var monthlyChart = new Chart(ctx1, {
            type: "bar",
            data: {
                labels: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],
                datasets: [{
                    label: "Revenue ($)",
                    data: [
                        ${requestScope.MONTH1}, ${requestScope.MONTH2}, ${requestScope.MONTH3},
                        ${requestScope.MONTH4}, ${requestScope.MONTH5}, ${requestScope.MONTH6},
                        ${requestScope.MONTH7}, ${requestScope.MONTH8}, ${requestScope.MONTH9},
                        ${requestScope.MONTH10}, ${requestScope.MONTH11}, ${requestScope.MONTH12}
                    ],
                    backgroundColor: "rgba(54, 162, 235, 0.7)",
                    borderColor: "rgba(54, 162, 235, 1)",
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                scales: {
                    y: {
                        beginAtZero: true,
                        title: {
                            display: true,
                            text: 'Revenue ($)'
                        }
                    }
                },
                plugins: {
                    legend: {
                        position: 'top'
                    }
                }
            }
        });

        // Yearly Revenue Chart
        var ctx2 = document.getElementById("yearly-revenue").getContext("2d");
        var yearlyChart = new Chart(ctx2, {
            type: "line",
            data: {
                labels: ["2018", "2019", "2020", "2021", "2022", "2023", "2024"],
                datasets: [{
                    label: "Revenue ($)",
                    data: [
                        ${requestScope.YEAR1}, ${requestScope.YEAR2}, ${requestScope.YEAR3},
                        ${requestScope.YEAR4}, ${requestScope.YEAR5}, ${requestScope.YEAR6},
                        ${requestScope.YEAR7}
                    ],
                    backgroundColor: "rgba(75, 192, 192, 0.2)",
                    borderColor: "rgba(75, 192, 192, 1)",
                    borderWidth: 2,
                    fill: true,
                    tension: 0.4
                }]
            },
            options: {
                responsive: true,
                scales: {
                    y: {
                        beginAtZero: true,
                        title: {
                            display: true,
                            text: 'Revenue ($)'
                        }
                    }
                },
                plugins: {
                    legend: {
                        position: 'top'
                    }
                }
            }
        });
    </script>
</body>
</html>