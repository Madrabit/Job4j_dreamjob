<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="ru.job4j.dream.model.Candidate" %>
<%@ page import="ru.job4j.dream.store.PsqlStore" %>
<%@ page import="ru.job4j.dream.store.CandidateStore" %>
<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.4.1.slim.min.js"
            integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n"
            crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
            integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo"
            crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
            integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6"
            crossorigin="anonymous"></script>
    <link rel="stylesheet" type="text/css" href="../style.css"/>
    <title>Работа мечты</title>
</head>
<body>
<%
    String id = request.getParameter("id");
    Candidate candid = new Candidate(0, "", "", "", "", "", "", "", "");
    if (id != null) {
        candid = CandidateStore.instOf().findCandidateById(Integer.parseInt(id));
    }
%>
<div class="container pt-3">
    <jsp:include page="/WEB-INF/header.jsp"/>
    <div class="row">
        <div class="card" style="width: 100%">
            <div class="card-header">
                <% if (id == null) { %>
                Новая вакансия.
                <% } else { %>
                Редактирование вакансии.
                <% } %>
            </div>
            <div class="card-body">
                <form action="<%=request.getContextPath()%>/candidates.do?id=<%=candid.getId()%>"
                      enctype="multipart/form-data" method="post">
                    <div class="form-group w-25 has-error">
                        <label for="name">Имя:</label>
                        <input type="text" class="form-control" id="name" name="name" value="<%=candid.getName()%>">
                        <div class="valid-inform  hidden" id="nameInvalid">Поле заполнено неверно</div>
                    </div>
                    <div class="form-group w-25">
                        <label for="lastName">Фамилия:</label>
                        <input type="text" class="form-control" name="lastName" id="lastName"
                               value="<%=candid.getLastName()%>">
                        <div class="valid-inform  hidden" id="lastNameInvalid">Поле заполнено неверно</div>
                    </div>
                    <div class="form-check">
                        <input class="form-check-input" type="radio" name="sex" id="male" value="Male"
                                <% if ("Male".equals(candid.getSex()) || "".equals(candid.getSex())) { %>
                               checked
                        <% } else { %>
                        <% } %>
                        <label class="form-check-label" for="male">
                            М
                        </label>
                    </div>
                    <div class="form-check">
                        <input class="form-check-input" type="radio" name="sex" id="female"
                               value="Female"
                            <% if ("Female".equals(candid.getSex())) { %>
                               checked
                            <% } else { %>
                            <% } %>
                        >
                        <label class="form-check-label" for="female">
                            Ж
                        </label>
                    </div>
                    Страна:
                    <select name="country" id="countySel" size="1">
                        <% if ("".equals(candid.getCountry())) { %>
                        <option value="" >Выбрать страну</option>
                        <% } else { %>
                        <option value="" ><%=candid.getCountry()%></option>
                        <% } %>
                    </select>

                    <br>
                    <br>
                    Регион: <select name="state" id="stateSel" size="1">
                    <% if ("".equals(candid.getRegion())) { %>
                    <option value="" >Выберите сначала Страну</option>
                    <% } else { %>
                    <option value="" ><%=candid.getRegion()%></option>
                    <% } %>
                </select>
                    <br>
                    <br>
                    Город: <select name="district" id="districtSel" size="1">
                    <% if ("".equals(candid.getCity())) { %>
                    <option value="" >Выберите сначала Регион</option>
                    <% } else { %>
                    <option value="" ><%=candid.getCity()%></option>
                    <% } %>
                </select>
                    <div class="form-group">
                        <label for="desc">Описание:</label>
                        <input type="textarea" class="form-control" name="description" id="desc"
                               value="<%=candid.getDescription()%>">
                    </div>
                    <div class="checkbox mb-3">
                        <input type="file" name="photoId">
                    </div>
                    <button type="submit" class="btn btn-primary">Сохранить</button>
                </form>
            </div>
        </div>
    </div>
</div>

<form name="myform" id="myForm">
</form>
<script>

    /**
     * Form validation.
     * @param event Submit event.
     */
    let validate = (event) => {
        document.querySelectorAll('.valid-inform').forEach(el => el.classList.add('hidden'));
        let name = document.querySelector('#name').value;
        let lastName = document.querySelector('#lastName').value;
        let isValid = true;
        if (name.trim() === '') {
            event.preventDefault()
            isValid = false;
            let error = document.querySelector('#nameInvalid');
            error.classList.remove('hidden');
        }
        if (lastName.trim() === '') {
            event.preventDefault()
            isValid = false;
            let error = document.querySelector('#lastNameInvalid');
            error.classList.remove('hidden');
        }
        if (isValid) {
            countySel[countySel.selectedIndex].value = countySel[countySel.selectedIndex].text
            stateSel[stateSel.selectedIndex].value = stateSel[stateSel.selectedIndex].text
            districtSel[districtSel.selectedIndex].value = districtSel[districtSel.selectedIndex].text
        }
        return isValid;
    }
    let form = document.querySelector('form')
    form.addEventListener('submit', validate);


    let countySel = document.getElementById("countySel");
    let stateSel = document.getElementById("stateSel");
    let districtSel = document.getElementById("districtSel");
    let oldCountry = '<%=candid.getCountry()%>';
    let oldRegion = '<%=candid.getRegion()%>';
    let oldCity = '<%=candid.getCity()%>';

    window.onload = function () {

        if (oldCountry != '' && oldRegion != '' && oldCity != '') {
            fetch('http://localhost:8081/dreamjob/cities',
                {
                    method: 'POST',
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({country: oldCountry, region: oldRegion})
                }
            ).then(response => response.json())
                .then(data => {
                    for (let key in data.country) {
                        if (oldCountry == data.country[key]) {
                            countySel.options[countySel.options.length] = new Option(data.country[key], key, true, true);
                        } else {
                            countySel.options[countySel.options.length] = new Option(data.country[key], key);
                        }
                    }
                    for (let key in data.region) {
                        if (oldRegion == key) {
                            stateSel.options[stateSel.options.length] = new Option(data.region[key], key, true, true);
                        } else {
                            stateSel.options[stateSel.options.length] = new Option(data.region[key], key);
                        }
                    }
                    for (let key in data.cities) {
                        if (oldCity == key) {
                            districtSel.options[districtSel.options.length] = new Option(data.cities[key], key, true, true);
                        } else {
                            districtSel.options[districtSel.options.length] = new Option(data.cities[key], key);
                        }
                    }
                }).catch(error => console.error(error))
        } else {
            fetch('http://localhost:8081/dreamjob/cities',
                {
                    method: 'POST',
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    }
                }
            ).then(response => response.json())
                .then(data => {
                    for (let key in data.country) {
                            countySel.options[countySel.options.length] = new Option(data.country[key], key);
                    }
                }).catch(error => console.error(error))
        }
    }


    countySel.onchange = function () {
        let value = countySel.options[countySel.selectedIndex].value;
        fetch('http://localhost:8081/dreamjob/cities',
            {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({country: value})
            }
        ).then(response => response.json())
            .then(data => {
                for (let key in data.regions) {
                    stateSel.options[stateSel.options.length] = new Option(data.regions[key], key);
                }
            })
            .catch(error => console.error(error))
        stateSel.length = 1;
        districtSel.length = 1;
        if (this.selectedIndex < 1) return;
    }

    stateSel.onchange = function () {
        let countryValue = countySel.options[countySel.selectedIndex].value;
        let stateValue = stateSel.options[stateSel.selectedIndex].value;
        fetch('http://localhost:8081/dreamjob/cities',
            {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({country: countryValue, region: stateValue})
            }
        ).then(response => response.json())
            .then(data => {
                for (let key in data.cities) {
                    districtSel.options[districtSel.options.length] = new Option(data.cities[key], key);
                }
            })
            .catch(error => console.error(error))
        districtSel.length = 1;
        if (this.selectedIndex < 1) return;
    }

</script>
</body>
</html>
