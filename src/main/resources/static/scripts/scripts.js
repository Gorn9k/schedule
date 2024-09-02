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