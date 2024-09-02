const current = document.getElementById('current');
const prev = document.getElementById('prev');
const next = document.getElementById('next');

current.textContent = generateFrontDatePeriod({startDateString: startDateString, endDateString: endDateString});

prev.href = prev.href.concat(generateDatePeriodParameters({dateString: startDateString, flag: -1}))
    .concat("&frame=").concat(frame);
next.href = next.href.concat(generateDatePeriodParameters({dateString: startDateString, flag: 1}))
    .concat("&frame=").concat(frame);

const headerContainer = document.getElementById("table-header-container");
const bodyContainer = document.getElementById("table-body-container");

function checkOverflow() {
    if (bodyContainer.scrollHeight > bodyContainer.clientHeight)
        headerContainer.style.paddingRight = "5px";
    else
        headerContainer.style.paddingRight = "0";
}

checkOverflow();

window.addEventListener('resize', checkOverflow);

function generateDatePeriodParameters({dateString, flag}) {
    let date;
    if (dateString == null)
        date = new Date();
    else {
        date = new Date(
            dateString.split("-")[0],
            dateString.split("-")[1] - 1,
            dateString.split("-")[2]);
        if (flag === -1)
            date.setDate(date.getDate() - 7);
        else if (flag === 1)
            date.setDate(date.getDate() + 7);
    }
    const firstDayOfWeek = date.getDate() - (date.getDay() === 0 ? 7 : date.getDay()) + 1;
    const firstDayOfWeekDate = new Date(date.setDate(firstDayOfWeek));
    const lastDayOfWeek = firstDayOfWeekDate.getDate() + 6;
    const lastDayOfWeekDate = new Date(date.setDate(lastDayOfWeek));

    const firstDayOfWeekString = `${firstDayOfWeekDate.getFullYear()}-${(firstDayOfWeekDate.getMonth() + 1).toString().padStart(2, '0')}-${firstDayOfWeekDate.getDate().toString().padStart(2, '0')}`;
    const lastDayOfWeekString = `${lastDayOfWeekDate.getFullYear()}-${(lastDayOfWeekDate.getMonth() + 1).toString().padStart(2, '0')}-${lastDayOfWeekDate.getDate().toString().padStart(2, '0')}`;

    return `startDate=${firstDayOfWeekString}&endDate=${lastDayOfWeekString}`
}

function generateFrontDatePeriod({startDateString, endDateString}) {
    const startDate = new Date(
        startDateString.split("-")[0],
        startDateString.split("-")[1] - 1,
        startDateString.split("-")[2]);
    const endDate = new Date(
        endDateString.split("-")[0],
        endDateString.split("-")[1] - 1,
        endDateString.split("-")[2]);

    const firstDayOfWeekString = `${startDate.getDate().toString().padStart(2, '0')}.${(startDate.getMonth() + 1).toString().padStart(2, '0')}.${startDate.getFullYear()}`;
    const lastDayOfWeekString = `${endDate.getDate().toString().padStart(2, '0')}.${(endDate.getMonth() + 1).toString().padStart(2, '0')}.${endDate.getFullYear()}`;

    return `${firstDayOfWeekString} - ${lastDayOfWeekString}`;
}

document.addEventListener("DOMContentLoaded", function() {
    mergeTableCells('schedule-table');
});

function mergeTableCells(tableId) {
    const table = document.getElementById(tableId);

    if (table == null)
        return

    const rows = table.getElementsByTagName('tr');

    if (rows.length > 0) {
        const colsCount = 1;

        for (let colIndex = 0; colIndex < colsCount; colIndex++) {
            let lastValue = '';
            let lastCell = rows[0].children[0];
            let rowspan = 1;

            for (let rowIndex = 0; rowIndex < rows.length; rowIndex++) {
                const cell = rows[rowIndex].children[colIndex];
                const cellValue = cell ? (cell.innerText || cell.textContent).trim() : '';

                if (cellValue === lastValue && cellValue !== '') {
                    rowspan++;
                    cell.style.display = 'none';
                    lastCell.rowSpan = rowspan;
                } else {
                    lastValue = cellValue;
                    lastCell = cell;
                    rowspan = 1;
                }
            }
        }
    }
}