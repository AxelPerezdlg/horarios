function comparaHoras() {

    const inicio = document.getElementById("horaInicial");
    const final = document.getElementById("horaUltima");

    const vInicio = inicio.value;
    const vFinal = final.value;
    const tIni = new Date();
    const pInicio = vInicio.split(":");
    tIni.setHours(pInicio[0], pInicio[1]);
    const tFin = new Date();
    const pFin = vFinal.split(":");
    tFin.setHours(pFin[0], pFin[1]);

    var fechaValidacion = $('#fechaDia').val();
    var horaIn = $('#horaInicial').val();
    var horaFi = $('#horaUltima').val();
    var repeticiones = $('#repeticiones').val();
    var usuarios = $('#selectUsuarios').val();
    var ventanillas = $('#selectVentanilla').val();

    var fechaDia = new Date($('#fechaDia').val()+ " 00:00:00");
    var today = new Date();
    today.setHours(0,0,0,0);

    var horaInSplit = horaIn.split(":");
    var horaFinSplit = horaFi.split(":");
    
    var startHour = 8;
    var endHour = 19;



   
    if (fechaValidacion === "") {
        new Toast({
            message: 'Por favor de agregar un dia para el horario',
            type: 'danger'
        });
    } else if (fechaDia.getTime() < today.getTime()) {
        new Toast({
            message: 'Por favor poner una fecha actual o futura',
            type: 'danger'
        });
    } else if (repeticiones === "") {
        new Toast({
            message: 'Por favor ingrese un número de repeticiones',
            type: 'danger'
        });
    } else if (repeticiones < 0) {
        new Toast({
            message: 'Solo se admiten numeros arriba de 0',
            type: 'danger'
        });
    } else if (usuarios === "") {
        new Toast({
            message: 'Por favor de escoger el usuario de atención para el horario',
            type: 'danger'
        });
    } else if (ventanillas === "") {
        new Toast({
            message: 'Por favor de escoger la ventanilla de atención para el horario',
            type: 'danger'
        });
    } else if (tFin.getTime() < tIni.getTime()) {

        new Toast({
            message: 'La hora final no debe ser Menor a la hora inicial',
            type: 'danger'
        });

    } else if (tFin.getTime() === tIni.getTime()) {

        new Toast({
            message: 'No puedes insertar las misma hora para la inicial y final',
            type: 'danger'
        });

    } else if (horaIn === "") {
        new Toast({
            message: 'Por favor de escoger la hora Inicial de atención para el horario',
            type: 'danger'
        });
    } else if (horaFi === "") {
        new Toast({
            message: 'Por favor de escoger la hora Final de atención para el horario',
            type: 'danger'
        });
    } else if((horaInSplit != "") && (horaInSplit[0] < startHour || horaInSplit[0] > endHour)) {
        new Toast({
            message: 'La hora inicial solo puede ser despues de las 8 de la mañana y antes de las 7 noche',
            type: 'danger'
        });
    }else if((horaFinSplit != "") && (horaFinSplit[0] < startHour || horaFinSplit[0] > endHour)){
        new Toast({
            message: 'La hora final solo puede ser despues de las 8 de la mañana y antes de las 7 de la noche',
            type: 'danger'
        });
    }else{
        var btn = document.getElementById("formRegistro")
        document.body.append(btn);
        btn.submit();
    }
}
    




