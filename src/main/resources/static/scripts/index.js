const firstFrameLink = document.getElementById('first-frame-link');
const fourthFrameLink = document.getElementById('fourth-frame-link');
const link219 = document.getElementById('219-link');
firstFrameLink.href = firstFrameLink.href.concat(generateDatePeriodParameters({flag: 0})).concat("&frame=FIRST");
fourthFrameLink.href = fourthFrameLink.href.concat(generateDatePeriodParameters({flag: 0})).concat("&frame=FOURTH");
link219.href = link219.href.concat(generateDatePeriodParameters({flag: 0}));

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